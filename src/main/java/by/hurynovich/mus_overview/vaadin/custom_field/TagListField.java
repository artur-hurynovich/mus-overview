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

public class TagListField extends CustomField<List<TagDTO>> {

    private final List<TagDTO> value;

    private final VerticalLayout parentLayout;

    private final List<HorizontalLayout> childLayouts;

    private final Button addTagButton;

    public TagListField(final String caption) {
        setCaption(caption);
        value = new ArrayList<>();
        parentLayout = new VerticalLayout();
        childLayouts = new ArrayList<>();
        addTagButton = new Button("Add Tag");
    }

    @Override
    protected Component initContent() {
        return getParentLayout();
    }

    @Override
    protected void doSetValue(final List<TagDTO> tagDTO) {
        value.clear();
        value.addAll(tagDTO);
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
        value.forEach(tagDTO -> {
            final HorizontalLayout horizontalLayout = new HorizontalLayout();
            final TextField textField = new TextField();
            textField.setValue(tagDTO.getName());
            final Button removeButton = new Button("Remove Tag");




            horizontalLayout.addComponents(textField, removeButton);
            childLayouts.add(horizontalLayout);
        });
        return childLayouts;
    }

    private Button getAddTagButton() {
        return addTagButton;
    }

}
