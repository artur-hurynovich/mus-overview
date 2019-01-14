package by.hurynovich.mus_overview.vaadin.form;

import by.hurynovich.mus_overview.dto.GroupDTO;
import by.hurynovich.mus_overview.exception.GroupCreationException;
import by.hurynovich.mus_overview.exception.GroupUpdatingException;
import by.hurynovich.mus_overview.service.GroupService;
import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.stream.Collectors;

public class GroupForm extends Panel {

    private final VerticalLayout parentLayout;

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
        parentLayout = new VerticalLayout();
        nameField = new TextField("Name:");
        nameField.focus();
        buttonsLayout = new HorizontalLayout();
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        binder.forField(nameField).withValidator(groupName -> groupName != null && !groupName.isEmpty(),
                "Please enter the group name!").bind(GroupDTO::getName, GroupDTO::setName);
        binder.readBean(groupDTO);
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
        saveButton.addClickListener(clickEvent -> {
            try {
                if (binder.writeBeanIfValid(groupDTO)) {
                    if (groupDTO.getId() == 0) {
                        groupService.createGroup(groupDTO);
                        Notification.show("Group \'" + groupDTO.getName() + "\' created!",
                                Notification.Type.HUMANIZED_MESSAGE);
                    } else {
                        groupService.updateGroup(groupDTO);
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
            } catch (GroupCreationException | GroupUpdatingException e) {
                Notification.show("Error!", "Group saving failed!",
                        Notification.Type.ERROR_MESSAGE);
            }
        });
        saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        return saveButton;
    }

    private Button getCancelButton(final Runnable onDiscard) {
        cancelButton.addClickListener(clickEvent -> onDiscard.run());
        return cancelButton;
    }

}
