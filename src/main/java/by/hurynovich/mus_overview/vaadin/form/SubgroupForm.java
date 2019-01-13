package by.hurynovich.mus_overview.vaadin.form;

import by.hurynovich.mus_overview.dto.GroupDTO;
import by.hurynovich.mus_overview.dto.SubgroupDTO;
import by.hurynovich.mus_overview.exception.SubgroupCreationException;
import by.hurynovich.mus_overview.exception.SubgroupUpdatingException;
import by.hurynovich.mus_overview.service.GroupService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

public class SubgroupForm extends Panel {

    private final VerticalLayout parentLayout;

    private final ComboBox<GroupDTO> groupField;

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
        groupField = new ComboBox<>("Group:");
        nameField = new TextField("Name");
        nameField.focus();
        buttonsLayout = new HorizontalLayout();
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        binder.bind(nameField, SubgroupDTO::getName, SubgroupDTO::setName);
        binder.readBean(subgroupDTO);
        setContent(getParentLayout(subgroupDTO, onSave, onDiscard));
    }

    private VerticalLayout getParentLayout(final SubgroupDTO subgroupDTO,
                                           final Runnable onSave, final Runnable onDiscard) {
        parentLayout.addComponents(getNameField(), getGroupField(subgroupDTO),
                getButtonsLayout(subgroupDTO, onSave, onDiscard));
        return parentLayout;
    }

    private ComboBox getGroupField(final SubgroupDTO subgroupDTO) {
        final List<GroupDTO> groups = groupService.getAllGroups();
        groupField.setItems(groups);
        final long groupId = subgroupDTO.getGroupId();
        if (groupId != 0) {
            final GroupDTO selectedGroup = groups.stream().
                    filter(groupDTO -> groupDTO.getId() == groupId).iterator().next();
            groupField.setSelectedItem(selectedGroup);
        }
        groupField.setItemCaptionGenerator(GroupDTO::getName);
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
                final GroupDTO selectedGroup = groupField.getValue();
                if (selectedGroup != null) {
                    final long groupId = selectedGroup.getId();
                    subgroupDTO.setGroupId(groupId);
                } else {
                    throw new SubgroupCreationException("Property 'groupId' is a foreign key constraint " +
                            "and, therefore, should be valid!", new IllegalArgumentException());
                }
                if (subgroupDTO.getId() == 0) {
                    binder.writeBean(subgroupDTO);
                    groupService.createSubgroup(subgroupDTO);
                } else {
                    binder.writeBean(subgroupDTO);
                    groupService.updateSubgroup(subgroupDTO);
                }
                onSave.run();
            } catch (ValidationException e) {
                Notification.show("Warning!", "Form is not valid!",
                        Notification.Type.WARNING_MESSAGE);
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
