package by.hurynovich.mus_overview.vaadin.form.impl;

import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.service.impl.UserDetailsServiceImpl;
import by.hurynovich.mus_overview.vaadin.custom_field.UserRoleField;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import com.vaadin.data.ValidationResult;
import com.vaadin.ui.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.stream.Collectors;

@Component("userForm")
public class UserForm extends AbstractDTOForm<UserDTO> {
    private final UserDetailsServiceImpl userService;
    private UserDTO userDTO;
    private UserRoleField roleField;
    private Runnable onSave;
    private Runnable onDiscard;

    @Autowired
    public UserForm(final @Qualifier("userService") UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        setupButtonsLayout();
        setupBinder();
        setupParentLayout();
        setContent(getParentLayout());
    }

    @Override
    public void setupForm(final UserDTO userDTO, final Runnable onSave, final Runnable onDiscard) {
        this.userDTO = userDTO;
        getBinder().readBean(userDTO);
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
            if (getBinder().writeBeanIfValid(userDTO)) {
                userService.update(userDTO);
                Notification.show("User role updated!",
                        Notification.Type.HUMANIZED_MESSAGE);
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
        getBinder().forField(getRoleField()).withValidator(Objects::nonNull,
                "Please, select the users role!").bind(UserDTO::getRole, UserDTO::setRole);
    }

    private void setupParentLayout() {
        getParentLayout().addComponents(getRoleField(), getButtonsLayout());
    }

    private UserRoleField getRoleField() {
        if (roleField == null) {
            roleField = new UserRoleField();
            roleField.setCaption("Role:");
            roleField.focus();
        }
        return roleField;
    }
}
