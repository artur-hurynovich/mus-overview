package by.hurynovich.mus_overview.vaadin.form.impl;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.service.impl.GroupService;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.stream.Collectors;

public class GroupForm extends AbstractDTOForm<GroupDTO> {

    private VerticalLayout parentLayout;

    private TextField nameField;

    private Binder<GroupDTO> binder;

    private HorizontalLayout buttonsLayout;

    private Button saveButton;

    private Button cancelButton;

    private GroupService groupService;

    public GroupForm() {
        /*this.groupService = groupService;
        binder = new Binder<>(GroupDTO.class);
        binder.forField(getNameField()).withValidator(groupName -> groupName != null && !groupName.isEmpty(),
                "Please enter the group name!").bind(GroupDTO::getName, GroupDTO::setName);
        binder.readBean(groupDTO);
        setContent(getParentLayout(groupDTO, onSave, onDiscard));*/
    }

    private VerticalLayout getParentLayout(final GroupDTO groupDTO,
                                           final Runnable onSave, final Runnable onDiscard) {
        if (parentLayout == null) {
            parentLayout = new VerticalLayout();
            parentLayout.addComponents(getNameField(), getButtonsLayout(groupDTO, onSave, onDiscard));
        }
        return parentLayout;
    }

    private TextField getNameField() {
        if (nameField == null) {
            nameField = new TextField("Name:");
            nameField.focus();
        }
        return nameField;
    }

    private HorizontalLayout getButtonsLayout(final GroupDTO groupDTO, final Runnable onSave,
                                              final Runnable onDiscard) {
        if (buttonsLayout == null) {
            buttonsLayout = new HorizontalLayout();
            buttonsLayout.addComponents(getSaveButton(groupDTO, onSave), getCancelButton(onDiscard));
        }
        return buttonsLayout;
    }

    private Button getSaveButton(final GroupDTO groupDTO, final Runnable onSave) {
        if (saveButton == null) {
            saveButton = new Button("Save");
            saveButton.addClickListener(clickEvent -> {
                if (binder.writeBeanIfValid(groupDTO)) {
                    if (groupDTO.getId() == 0) {
                        groupService.save(groupDTO);
                        Notification.show("Group \'" + groupDTO.getName() + "\' created!",
                                Notification.Type.HUMANIZED_MESSAGE);
                    } else {
                        groupService.update(groupDTO);
                        Notification.show("Group updated!",
                                Notification.Type.HUMANIZED_MESSAGE);
                    }
                    onSave.run();
                } else {
                    final String validationError = binder.validate().getValidationErrors().stream().
                            map(ValidationResult::getErrorMessage).collect(Collectors.joining("; "));
                    Notification.show("Warning!\n" + validationError,
                            Notification.Type.WARNING_MESSAGE);
                }
            });
            saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        }
        return saveButton;
    }

    private Button getCancelButton(final Runnable onDiscard) {
        if (cancelButton == null) {
            cancelButton = new Button("Cancel");
            cancelButton.addClickListener(clickEvent -> onDiscard.run());
        }
        return cancelButton;
    }

}
