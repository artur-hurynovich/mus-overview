package by.hurynovich.mus_overview.vaadin.from;

import by.hurynovich.mus_overview.dto.GroupDTO;
import by.hurynovich.mus_overview.service.GroupService;
import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class GroupForm extends Panel {

    private final VerticalLayout parentLayout;

    @PropertyId("name")
    private final TextField nameField;

    private final Binder<GroupDTO> binder;

    private final HorizontalLayout buttonsLayout;

    private final Button saveButton;

    private final Button cancelButton;

    private final GroupService groupService;

    public GroupForm(final GroupService groupService, final GroupDTO groupDTO,
                     final Runnable onSave, final Runnable onDiscard) {
        this.groupService = groupService;
        binder = new Binder<>(GroupDTO.class);
        binder.setBean(groupDTO);
        binder.bindInstanceFields(this);
        parentLayout = new VerticalLayout();
        nameField = new TextField("Name:");
        buttonsLayout = new HorizontalLayout();
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        setContent(getParentLayout(groupDTO, onSave, onDiscard));
    }

    private VerticalLayout getParentLayout(final GroupDTO groupDTO,
                                           final Runnable onSave, final Runnable onDiscard) {
        parentLayout.addComponents(getNameField(), getButtonsLayout(groupDTO, onSave, onDiscard));
        return parentLayout;
    }

    private TextField getNameField() {
        return nameField;
    }

    private HorizontalLayout getButtonsLayout(final GroupDTO groupDTO, final Runnable onSave,
                                              final Runnable onDiscard) {
        buttonsLayout.addComponents(getSaveButton(groupDTO, onSave), getCancelButton(onDiscard));
        return buttonsLayout;
    }

    private Button getSaveButton(final GroupDTO groupDTO, final Runnable onSave) {
        return saveButton;
    }

    private Button getCancelButton(final Runnable onDiscard) {
        return cancelButton;
    }

}
