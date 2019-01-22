package by.hurynovich.mus_overview.vaadin.custom_field;

import by.hurynovich.mus_overview.dto.impl.TagDTO;
import by.hurynovich.mus_overview.service.impl.TagDTOServiceImpl;
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

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Component("overviewTagField")
public class OverviewTagField extends CustomField<List<TagDTO>> {
    private final TagDTOServiceImpl tagService;
    private VerticalLayout parentLayout;
    private List<HorizontalLayout> childLayouts;
    private VerticalLayout addTagLayout;
    private RadioButtonGroup<String> radioButtonGroup;
    private ComboBox<TagDTO> availableTagsComboBox;
    private HorizontalLayout newTagLayout;
    private List<TagDTO> availableTagsList;
    private List<TagDTO> value;
    private final static String RADIO_EXISTING;
    private final static String RADIO_NEW;
    static {
        RADIO_EXISTING = "Add an existing tag";
        RADIO_NEW = "Add a new tag";
    }

    @Autowired
    public OverviewTagField(final @Qualifier("tagService") TagDTOServiceImpl tagService) {
        this.tagService = tagService;
    }

    @Override
    protected Component initContent() {
        setCaption("Tags:");
        return getParentLayout();
    }

    @Override
    protected void doSetValue(final List<TagDTO> tags) {
        value = tags;
        getAvailableTagsList().clear();
        getAvailableTagsList().addAll(tagService.findAll());
        if (getValue() == null || getValue().size() == 0) {
            getAvailableTagsComboBox().setItems(getAvailableTagsList());
            getChildLayouts().forEach(getParentLayout()::removeComponent);
            getChildLayouts().clear();
        } else {
            getAvailableTagsList().removeAll(getValue());
            getAvailableTagsComboBox().setItems(getAvailableTagsList());
            getChildLayouts().forEach(getParentLayout()::removeComponent);
            getChildLayouts().clear();
            getValue().forEach(tagDTO -> getChildLayouts().add(getChildLayout(tagDTO)));
            getChildLayouts().forEach(getParentLayout()::addComponent);
        }
    }

    private HorizontalLayout getChildLayout(final TagDTO tagDTO) {
        final HorizontalLayout childLayout = new HorizontalLayout();
        final Label label = new Label(tagDTO.getName());
        final Button deleteButton = new Button("X");
        deleteButton.addClickListener(clickEvent -> {
            getParentLayout().removeComponent(childLayout);
            getChildLayouts().remove(childLayout);
            getValue().remove(tagDTO);
            if (tagService.findByName(tagDTO.getName()) != null) {
                getAvailableTagsList().add(tagDTO);
                getAvailableTagsComboBox().setItems(getAvailableTagsList());
            }
        });
        childLayout.addComponents(label, deleteButton);
        return childLayout;
    }

    @Override
    public List<TagDTO> getValue() {
        return value;
    }

    private VerticalLayout getParentLayout() {
        if (parentLayout == null) {
            parentLayout = new VerticalLayout();
            getChildLayouts().forEach(parentLayout::addComponent);
            parentLayout.addComponents(getAddTagLayout());
            parentLayout.setMargin(false);
        }
        return parentLayout;
    }

    private List<HorizontalLayout> getChildLayouts() {
        if (childLayouts == null) {
            childLayouts = new ArrayList<>();
        }
        return childLayouts;
    }

    private VerticalLayout getAddTagLayout() {
        if (addTagLayout == null) {
            addTagLayout = new VerticalLayout();
            addTagLayout.addComponents(getRadioButtonGroup(), getAvailableTagsComboBox(), getNewTagLayout());
        }
        return addTagLayout;
    }

    private RadioButtonGroup<String> getRadioButtonGroup() {
        if (radioButtonGroup == null) {
            radioButtonGroup = new RadioButtonGroup<>();
            radioButtonGroup.setItems(RADIO_EXISTING, RADIO_NEW);
            radioButtonGroup.setSelectedItem(RADIO_EXISTING);
            radioButtonGroup.addSelectionListener(singleSelectionEvent -> {
                ((TextField) getNewTagLayout().getComponent(0)).clear();
                getAvailableTagsComboBox().setVisible(singleSelectionEvent.getValue().equals(RADIO_EXISTING));
                getNewTagLayout().setVisible(singleSelectionEvent.getValue().equals(RADIO_NEW));
            });
        }
        return radioButtonGroup;
    }

    private ComboBox<TagDTO> getAvailableTagsComboBox() {
        if (availableTagsComboBox == null) {
            availableTagsComboBox = new ComboBox<>();
            availableTagsComboBox.setItems(getAvailableTagsList());
            availableTagsComboBox.setItemCaptionGenerator(TagDTO::getName);
            availableTagsComboBox.addValueChangeListener(valueChangeEvent -> {
                final TagDTO tagDTO = valueChangeEvent.getValue();
                if (tagDTO != null) {
                    getAvailableTagsList().remove(tagDTO);
                    getAvailableTagsComboBox().setItems(getAvailableTagsList());
                    getValue().add(tagDTO);
                    final HorizontalLayout childLayout = getChildLayout(tagDTO);
                    getChildLayouts().add(childLayout);
                    getParentLayout().addComponent(childLayout);
                    availableTagsComboBox.setSelectedItem(null);
                }
            });
        }
        return availableTagsComboBox;
    }

    private List<TagDTO> getAvailableTagsList() {
        if (availableTagsList == null) {
            availableTagsList = tagService.findAll();
        }
        return availableTagsList;
    }

    private HorizontalLayout getNewTagLayout() {
        if (newTagLayout == null) {
            newTagLayout = new HorizontalLayout();
            final TextField textField = new TextField();
            final Button addButton = new Button("Add");
            addButton.setEnabled(false);
            textField.addValueChangeListener(valueChangeEvent -> {
                if (valueChangeEvent.getValue().isEmpty()) {
                    addButton.setEnabled(false);
                } else {
                    addButton.setEnabled(true);
                }
            });
            addButton.addClickListener(clickEvent -> {
                final TagDTO tagDTO = new TagDTO();
                tagDTO.setName(textField.getValue());
                textField.clear();
                final HorizontalLayout childLayout = getChildLayout(tagDTO);
                getChildLayouts().add(childLayout);
                getParentLayout().addComponent(childLayout);
                getValue().add(tagDTO);
            });
            newTagLayout.addComponents(textField, addButton);
            newTagLayout.setVisible(false);
        }
        return newTagLayout;
    }

}
