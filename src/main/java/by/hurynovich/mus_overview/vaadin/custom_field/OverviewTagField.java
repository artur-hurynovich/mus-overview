package by.hurynovich.mus_overview.vaadin.custom_field;

import by.hurynovich.mus_overview.dto.impl.TagDTO;
import by.hurynovich.mus_overview.service.impl.TagService;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.spring.annotation.PrototypeScope;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Component("overviewTagField")
@PrototypeScope
public class OverviewTagField extends CustomField<List<TagDTO>> {

    @Autowired
    @Qualifier("tagService")
    private TagService tagService;

    private VerticalLayout parentLayout;

    private List<HorizontalLayout> childLayouts;

    private VerticalLayout addTagLayout;

    private ComboBox<TagDTO> availableTagsComboBox;

    private List<TagDTO> availableTagsList;

    private HorizontalLayout newTagLayout;

    private final static String RADIO_EXISTING;

    private final static String RADIO_NEW;

    static {
        RADIO_EXISTING = "Add an existing tag";
        RADIO_NEW = "Add a new tag";
    }

    private List<TagDTO> value;

    @Override
    protected Component initContent() {
        availableTagsList = tagService.findAll();
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
        final Label tagNameLabel = new Label();
        tagNameLabel.setValue(tagDTO.getName());
        final Button removeButton = new Button("Remove");
        removeButton.addClickListener(clickEvent -> {
            final String tagName = tagNameLabel.getValue();
            removeTagFromValue(tagService.findByName(tagName));
            getChildLayouts().remove(childLayout);
            parentLayout.removeComponent(childLayout);
        });
        childLayout.addComponents(tagNameLabel, removeButton);
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
                    getAvailableTagsComboBox().setVisible(true);
                    getNewTagLayout().setVisible(false);
                } else {
                    getAvailableTagsComboBox().setVisible(false);
                    getNewTagLayout().setVisible(true);
                }
            });
            addTagLayout.addComponents(radioButtonGroup, getAvailableTagsComboBox(), getNewTagLayout());
        }
        return addTagLayout;
    }

    private ComboBox<TagDTO> getAvailableTagsComboBox() {
        if (availableTagsComboBox == null) {
            availableTagsComboBox = new ComboBox<>();
            availableTagsList.removeAll(getValue());
            availableTagsComboBox.setItems(availableTagsList);
            availableTagsComboBox.setItemCaptionGenerator(TagDTO::getName);
            availableTagsComboBox.addValueChangeListener(valueChangeEvent -> {
                final TagDTO selectedTag = valueChangeEvent.getValue();
                if (selectedTag != null) {
                    getChildLayouts().add(getChildLayout(selectedTag));
                    getValue().add(selectedTag);
                    availableTagsList.remove(selectedTag);
                    availableTagsComboBox.setItems(availableTagsList);
                    availableTagsComboBox.setSelectedItem(null);
                }
            });
        }
        return availableTagsComboBox;
    }

    private HorizontalLayout getNewTagLayout() {
        if (newTagLayout == null) {
            newTagLayout = new HorizontalLayout();
            final TextField textField = new TextField();
            final Button addTagButton = new Button("Add Tag");
            addTagButton.addClickListener(clickEvent -> {
                final TagDTO newTag = new TagDTO();
                newTag.setName(textField.getValue());
                addTagToValue(newTag);
                textField.clear();
            });
            newTagLayout.addComponents(textField, addTagButton);
            newTagLayout.setVisible(false);
        }
        return newTagLayout;
    }

    private void addTagToValue(final TagDTO tagDTO) {
        getValue().add(tagDTO);
        availableTagsList.remove(tagDTO);
        availableTagsComboBox.setItems(availableTagsList);
        final HorizontalLayout childLayout = getChildLayout(tagDTO);
        parentLayout.removeComponent(getAddTagLayout());
        parentLayout.addComponent(childLayout);
        parentLayout.addComponent(getAddTagLayout());
    }

    private void removeTagFromValue(final TagDTO tagDTO) {
        getValue().remove(tagDTO);
        if (tagDTO != null) {
            availableTagsList.add(tagDTO);
            availableTagsComboBox.setItems(availableTagsList);
        }
    }

}
