package by.hurynovich.mus_overview.vaadin.form;

import by.hurynovich.mus_overview.dto.GroupDTO;
import by.hurynovich.mus_overview.dto.SubgroupDTO;
import by.hurynovich.mus_overview.exception.SubgroupCreationException;
import by.hurynovich.mus_overview.exception.SubgroupUpdatingException;
import by.hurynovich.mus_overview.service.GroupService;
import by.hurynovich.mus_overview.vaadin.custom_field.SubgroupGroupField;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.List;
import java.util.stream.Collectors;

public class SubgroupForm extends Panel {

    private final VerticalLayout parentLayout;

    private final SubgroupGroupField groupField;

    private final TextField nameField;

    private final Binder<SubgroupDTO> binder;

    private final HorizontalLayout buttonsLayout;

    private final Button saveButton;

    private final Button cancelButton;

    private final GroupService groupService;

    public SubgroupForm(final GroupService groupService, final SubgroupDTO subgroupDTO,
                     final Runnable onSave, final Runnable onDiscard) {
        this.groupService = groupService;
        binder = new Binder<>(SubgroupDTO.class);
        parentLayout = new VerticalLayout();
        groupField = new SubgroupGroupField(groupService);
        groupField.setCaption("Group:");
        nameField = new TextField("Name");
        nameField.focus();
        buttonsLayout = new HorizontalLayout();
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        binder.forField(nameField).withValidator(subgroupName -> subgroupName != null && !subgroupName.isEmpty(),
                "Subgroup name can't be empty!").bind(SubgroupDTO::getName, SubgroupDTO::setName);
        binder.forField(groupField).withValidator(groupId -> groupId != null && groupId != 0,
                "Please choose the corresponding group!").bind(SubgroupDTO::getGroupId, SubgroupDTO::setGroupId);
        binder.readBean(subgroupDTO);
        setContent(getParentLayout(subgroupDTO, onSave, onDiscard));
    }

    private VerticalLayout getParentLayout(final SubgroupDTO subgroupDTO,
                                           final Runnable onSave, final Runnable onDiscard) {
        parentLayout.addComponents(getNameField(), getGroupField(),
                getButtonsLayout(subgroupDTO, onSave, onDiscard));
        return parentLayout;
    }

    private SubgroupGroupField getGroupField() {
        return groupField;
    }

    private TextField getNameField() {
        return nameField;
    }

    private HorizontalLayout getButtonsLayout(final SubgroupDTO subgroupDTO, final Runnable onSave,
                                              final Runnable onDiscard) {
        buttonsLayout.addComponents(getSaveButton(subgroupDTO, onSave), getCancelButton(onDiscard));
        return buttonsLayout;
    }

    private Button getSaveButton(final SubgroupDTO subgroupDTO, final Runnable onSave) {
        saveButton.addClickListener(clickEvent -> {
            try {
                if (binder.writeBeanIfValid(subgroupDTO)) {
                    if (subgroupDTO.getId() == 0) {
                        groupService.createSubgroup(subgroupDTO);
                        Notification.show("Subgroup \'" + subgroupDTO.getName() + "\' created!",
                                Notification.Type.ASSISTIVE_NOTIFICATION);
                    } else {
                        groupService.updateSubgroup(subgroupDTO);
                        Notification.show("Subgroup updated!",
                                Notification.Type.ASSISTIVE_NOTIFICATION);
                    }
                    onSave.run();
                } else {
                    String validationError = binder.validate().getValidationErrors().stream().
                            map(ValidationResult::getErrorMessage).collect(Collectors.joining("; "));
                    Notification.show("Warning!\n" + validationError,
                            Notification.Type.WARNING_MESSAGE);
                }
            } catch (SubgroupCreationException | SubgroupUpdatingException e) {
                Notification.show("Error!", "Subgroup saving failed!",
                        Notification.Type.ERROR_MESSAGE);
            }
        });
        saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        return saveButton;
    }

    private Button getCancelButton(final Runnable onDiscard) {
        cancelButton.addClickListener(clickEvent -> onDiscard.run());
        cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        return cancelButton;
    }

}
