package by.hurynovich.mus_overview.vaadin.form.impl;

import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.service.impl.UserService;
import by.hurynovich.mus_overview.vaadin.custom_field.UserRoleField;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.Objects;
import java.util.stream.Collectors;

public class SignUpForm extends AbstractDTOForm<UserDTO> {

    private TextField nameField;

    private TextField emailField;

    private PasswordField passwordField;

    private UserRoleField userRoleField;

    private Binder<UserDTO> binder;

    private Button signUpButton;

    private UserService userService;

    public SignUpForm() {
        /*this.userService = userService;
        nameField = new TextField("Name:");
        emailField = new TextField("E-mail:");
        passwordField = new PasswordField("Password:");
        userRoleField = new UserRoleField("User Role:");
        binder = getBinder();
        binder.readBean(userDTO);
        addComponents(nameField, emailField, passwordField, userRoleField, getSignUpButton(userDTO));*/
    }

    public Binder<UserDTO> getBinder() {
        if (binder == null) {
            binder = new Binder<>(UserDTO.class);
            binder.forField(nameField).withValidator(name -> name != null && name.length() > 4,
                    "Please enter a name with minimal length of 5 characters!").
                    bind(UserDTO::getName, UserDTO::setName);
            binder.forField(emailField).withValidator(new EmailValidator("Please enter a valid e-mail!")).
                    bind(UserDTO::getEmail, UserDTO::setEmail);
            binder.forField(passwordField).withValidator(password -> password != null && password.length() > 7,
                    "Please enter a password with minimal length of 8 characters!").
                    bind(UserDTO::getPassword, UserDTO::setPassword);
            binder.forField(userRoleField).withValidator(Objects::nonNull, "Please choose user role!").
                    bind(UserDTO::getRole, UserDTO::setRole);
        }
        return binder;
    }

    private Button getSignUpButton(final UserDTO userDTO) {
        if (signUpButton == null) {
            signUpButton = new Button("Sign Up");
            signUpButton.addClickListener(clickEvent -> {
                if (binder.writeBeanIfValid(userDTO)) {
                    final String email = userDTO.getEmail();
                    if (userService.emailExists(email)) {
                        Notification.show("Warning!\n" + "User with E-mail \'" + userDTO.getEmail() +
                                "\' already exists!",
                                Notification.Type.WARNING_MESSAGE);
                    } else {
                        userService.save(userDTO);
                        Notification.show("User \'" + userDTO.getName() + "\' signed up!",
                                Notification.Type.HUMANIZED_MESSAGE);
                    }
                } else {
                    final String validationError = binder.validate().getValidationErrors().stream().
                            map(ValidationResult::getErrorMessage).collect(Collectors.joining("; "));
                    Notification.show("Warning!\n" + validationError,
                            Notification.Type.WARNING_MESSAGE);
                }
            });
        }
        return signUpButton;
    }

}
