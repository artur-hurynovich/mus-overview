package by.hurynovich.mus_overview.vaadin.custom_field;

import by.hurynovich.mus_overview.dto.impl.TagDTO;
import by.hurynovich.mus_overview.service.impl.TagService;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Component("overviewTagField")
public class OverviewTagField extends CustomField<List<TagDTO>> {

    private final TagService tagService;

    private VerticalLayout parentLayout;

    private List<HorizontalLayout> childLayouts;

    private VerticalLayout addTagLayout;

    private ComboBox<TagDTO> existingTagsComboBox;

    private ListDataProvider<TagDTO> existingTagsDataProvider;

    private List<TagDTO> tagDTOList;

    private HorizontalLayout newTagLayout;

    private final static String RADIO_EXISTING;

    private final static String RADIO_NEW;

    static {
        RADIO_EXISTING = "Add an existing tag";
        RADIO_NEW = "Add a new tag";
    }

    private List<TagDTO> value;

    public OverviewTagField(final TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    protected Component initContent() {
        return getParentLayout();
    }

    @Override
    protected void doSetValue(final List<TagDTO> tags) {
        value = tags;
    }

    @Override
    public List<TagDTO> getValue() {
        return value;
    }

    private VerticalLayout getParentLayout() {
        if (parentLayout == null) {
            parentLayout = new VerticalLayout();
            getChildLayouts().forEach(parentLayout::addComponent);
            parentLayout.addComponent(getAddTagLayout());
        }
        return parentLayout;
    }

    private List<HorizontalLayout> getChildLayouts() {
        if (childLayouts == null) {
            childLayouts = new ArrayList<>();
            if (value != null) {
                getValue().forEach(tagDTO -> childLayouts.add(getChildLayout(tagDTO)));
            }
        }
        return childLayouts;
    }

    private HorizontalLayout getChildLayout(final TagDTO tagDTO) {
        final HorizontalLayout childLayout = new HorizontalLayout();
        final Label tagName = new Label();
        tagName.setValue(tagDTO.getName());
        final Button removeButton = new Button("Remove");
        removeButton.addClickListener(getRemoveButtonClickListener(tagName, childLayout));
        childLayout.addComponents(tagName, removeButton);
        return childLayout;
    }

    private VerticalLayout getAddTagLayout() {
        if (addTagLayout == null) {
            addTagLayout = new VerticalLayout();
            final RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>("Add a Tag:");
            radioButtonGroup.setItems(RADIO_EXISTING, RADIO_NEW);
            radioButtonGroup.setSelectedItem(RADIO_EXISTING);
            radioButtonGroup.addSelectionListener(singleSelectionEvent -> {
                if (singleSelectionEvent.getValue().equals(RADIO_EXISTING)) {
                    getExistingTagsComboBox().setVisible(true);
                    getNewTagLayout().setVisible(false);
                } else {
                    getExistingTagsComboBox().setVisible(false);
                    getNewTagLayout().setVisible(true);
                }
            });
            addTagLayout.addComponents(radioButtonGroup, getExistingTagsComboBox(), getNewTagLayout());
        }
        return addTagLayout;
    }

    private ComboBox<TagDTO> getExistingTagsComboBox() {
        if (existingTagsComboBox == null) {
            existingTagsComboBox = new ComboBox<>();
            existingTagsComboBox.setDataProvider(getExistingTagsDataProvider());
            existingTagsComboBox.getDataProvider().refreshAll();
            existingTagsComboBox.setItemCaptionGenerator(TagDTO::getName);
            existingTagsComboBox.addValueChangeListener(valueChangeEvent -> {
                final TagDTO selectedTag = valueChangeEvent.getValue();
                if (selectedTag != null) {
                    getChildLayouts().add(getChildLayout(selectedTag));
                    tagDTOList.remove(selectedTag);
                    addTag(selectedTag);
                    existingTagsDataProvider = DataProvider.ofCollection(tagDTOList);
                    existingTagsComboBox.setDataProvider(existingTagsDataProvider);
                    existingTagsComboBox.getDataProvider().refreshAll();
                }
                existingTagsComboBox.setSelectedItem(null);
            });
        }
        return existingTagsComboBox;
    }

    private ListDataProvider<TagDTO> getExistingTagsDataProvider() {
        if (existingTagsDataProvider == null) {
            tagDTOList = tagService.findAll();
            tagDTOList.removeAll(getValue());
            existingTagsDataProvider = DataProvider.ofCollection(tagDTOList);
        }
        return existingTagsDataProvider;
    }

    private HorizontalLayout getNewTagLayout() {
        if (newTagLayout == null) {
            newTagLayout = new HorizontalLayout();
            final TextField textField = new TextField();
            final Button addTagButton = new Button("Add Tag");
            addTagButton.addClickListener(clickEvent -> {
                final TagDTO newTag = new TagDTO();
                newTag.setName(textField.getValue());
                addTag(newTag);
                textField.clear();
            });
            newTagLayout.addComponents(textField, addTagButton);
            newTagLayout.setVisible(false);
        }
        return newTagLayout;
    }

    private Button.ClickListener getRemoveButtonClickListener(final Label tagName,
                                                              final HorizontalLayout childLayout) {
        return clickEvent -> {
            final String tagToDeleteName = tagName.getValue();
            final List<TagDTO> tags = getValue();
            tags.removeIf(tagDTO -> tagDTO.getName().equals(tagToDeleteName));
            setValue(tags);
            getChildLayouts().remove(childLayout);
            parentLayout.removeComponent(childLayout);
            TagDTO tagDTO = tagService.findByName(tagToDeleteName);
            if (tagDTO != null) {
                tagDTOList.add(tagDTO);
                existingTagsDataProvider = DataProvider.ofCollection(tagDTOList);
                getExistingTagsComboBox().getDataProvider().refreshAll();
            }
        };
    }

    private void addTag(final TagDTO tagDTO) {
        getValue().add(tagDTO);
        final HorizontalLayout childLayout = getChildLayout(tagDTO);
        parentLayout.removeComponent(getAddTagLayout());
        parentLayout.addComponent(childLayout);
        parentLayout.addComponent(getAddTagLayout());
    }

}
