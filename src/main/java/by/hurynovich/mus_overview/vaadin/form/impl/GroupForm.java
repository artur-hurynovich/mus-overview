package by.hurynovich.mus_overview.vaadin.form.impl;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.service.IGroupDTOService;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import com.vaadin.data.ValidationResult;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Component("groupForm")
public class GroupForm extends AbstractDTOForm<GroupDTO> {

    @Autowired
    @Qualifier("groupService")
    private IGroupDTOService groupService;

    private GroupDTO groupDTO;

    private TextField nameField;

    private Runnable onSave;

    private Runnable onDiscard;

    @PostConstruct
    public void init() {
        setupButtonsLayout();
        setupBinder();
        setupParentLayout();
        setContent(getParentLayout());
    }

    @Override
    public void setupForm(final GroupDTO groupDTO, final Runnable onSave, final Runnable onDiscard) {
        this.groupDTO = groupDTO;
        getBinder().readBean(groupDTO);
        this.onSave = onSave;
        this.onDiscard = onDiscard;
    }

    private void setupButtonsLayout() {
        setupSaveButton();
        setupCancelButton();
        getButtonsLayout().addComponents(getSaveButton(), getCancelButton());
    }

    private void setupSaveButton() {
        getSaveButton().addClickListener(clickEvent -> {
            if (getBinder().writeBeanIfValid(groupDTO)) {
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
                final String validationError = getBinder().validate().getValidationErrors().stream().
                        map(ValidationResult::getErrorMessage).collect(Collectors.joining(";\n"));
                Notification.show("Warning!\n" + validationError,
                        Notification.Type.WARNING_MESSAGE);
            }
        });
    }

    private void setupCancelButton() {
        getCancelButton().addClickListener(clickEvent -> onDiscard.run());
    }

    private void setupBinder() {
        getBinder().forField(getNameField()).withValidator(name -> name != null && !name.isEmpty(),
                "Please, enter the group name!").bind(GroupDTO::getName, GroupDTO::setName);
    }

    private void setupParentLayout() {
        getParentLayout().addComponents(getNameField(), getButtonsLayout());
    }

    private TextField getNameField() {
        if (nameField == null) {
            nameField = new TextField("Name:");
            nameField.focus();
        }
        return nameField;
    }

}
