package by.hurynovich.mus_overview.vaadin.form;

import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.service.impl.GroupService;
import by.hurynovich.mus_overview.service.impl.SubgroupService;
import by.hurynovich.mus_overview.vaadin.custom_field.SubgroupGroupField;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

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

    private final SubgroupService subgroupService;

    public SubgroupForm(final GroupService groupService, final SubgroupService subgroupService,
                        final SubgroupDTO subgroupDTO, final Runnable onSave, final Runnable onDiscard) {
        this.groupService = groupService;
        this.subgroupService = subgroupService;
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
            if (binder.writeBeanIfValid(subgroupDTO)) {
                if (subgroupDTO.getId() == 0) {
                    subgroupService.save(subgroupDTO);
                    Notification.show("Subgroup \'" + subgroupDTO.getName() + "\' created!",
                            Notification.Type.HUMANIZED_MESSAGE);
                } else {
                    subgroupService.update(subgroupDTO);
                    Notification.show("Subgroup updated!",
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
        return saveButton;
    }

    private Button getCancelButton(final Runnable onDiscard) {
        cancelButton.addClickListener(clickEvent -> onDiscard.run());
        cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        return cancelButton;
    }

}
