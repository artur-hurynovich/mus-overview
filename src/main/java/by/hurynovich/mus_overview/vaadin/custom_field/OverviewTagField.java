package by.hurynovich.mus_overview.vaadin.custom_field;

import by.hurynovich.mus_overview.dto.TagDTO;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class OverviewTagField extends CustomField<List<TagDTO>> {

    private final VerticalLayout parentLayout;

    private final List<HorizontalLayout> childLayouts;

    private final Button addTagButton;

    private List<TagDTO> value;

    public OverviewTagField(final String caption) {
        setCaption(caption);
        parentLayout = new VerticalLayout();
        childLayouts = new ArrayList<>();
        addTagButton = new Button("Add Tag");
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
        getChildLayouts().forEach(parentLayout::addComponent);
        parentLayout.addComponent(getAddTagButton());
        return parentLayout;
    }

    private List<HorizontalLayout> getChildLayouts() {
        if (value != null) {
            getValue().forEach(tagDTO -> {
                final HorizontalLayout childLayout = new HorizontalLayout();
                final TextField textField = new TextField();
                textField.setValue(tagDTO.getName());
                textField.addValueChangeListener(valueChangeEvent -> {
                    final String newTextFieldValue = textField.getValue();
                    final int index = getChildLayouts().indexOf(childLayout);
                    final List<TagDTO> tagsToUpdate = getValue();
                    tagsToUpdate.get(index).setName(newTextFieldValue);
                    setValue(tagsToUpdate);
                });
                final Button removeButton = new Button("Remove");
                removeButton.addClickListener(getRemoveButtonClickListener(textField, childLayout));
                childLayout.addComponents(textField, removeButton);
                childLayouts.add(childLayout);
            });
        }
        return childLayouts;
    }

    private Button getAddTagButton() {
        addTagButton.addClickListener(getAddTagButtonClickListener());
        return addTagButton;
    }

    private Button.ClickListener getRemoveButtonClickListener(final TextField textField,
                                                              final HorizontalLayout childLayout) {
        return clickEvent -> {
            final String tagToDeleteName = textField.getValue();
            final List<TagDTO> tags = getValue();
            tags.removeIf(tagDTO -> tagDTO.getName().equals(tagToDeleteName));
            setValue(tags);
            getChildLayouts().remove(childLayout);
            parentLayout.removeComponent(childLayout);
        };
    }

    private Button.ClickListener getAddTagButtonClickListener() {
        return clickEvent -> {
            final List<TagDTO> tags = getValue();
            final HorizontalLayout childLayout = new HorizontalLayout();
            final TextField textField = new TextField();
            textField.addValueChangeListener(getTextFieldValueChangeListener(textField, childLayout));
            final Button removeButton = new Button("Remove");
            removeButton.addClickListener(getRemoveButtonClickListener(textField, childLayout));
            childLayout.addComponents(textField, removeButton);
            childLayouts.add(childLayout);
            tags.add(new TagDTO());
            setValue(tags);
            parentLayout.removeComponent(addTagButton);
            parentLayout.addComponent(childLayout);
            parentLayout.addComponent(addTagButton);
        };
    }

    private ValueChangeListener<String> getTextFieldValueChangeListener(final TextField textField,
                                                                final HorizontalLayout childLayout) {
        return valueChangeEvent -> {
            final String newTagName = textField.getValue();
            final int index = getChildLayouts().indexOf(childLayout);
            final List<TagDTO> tagsToUpdate = getValue();
            tagsToUpdate.get(index).setName(newTagName);
            setValue(tagsToUpdate);
        };
    }

}
