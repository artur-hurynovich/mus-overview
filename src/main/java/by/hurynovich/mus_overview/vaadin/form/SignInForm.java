package by.hurynovich.mus_overview.vaadin.form;

import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.service.impl.UserService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.stream.Collectors;

public class SignInForm extends VerticalLayout {

    private TextField emailField;

    private PasswordField passwordField;

    private Binder<UserDTO> binder;

    private Button signInButton;

    private final UserService userService;

    public SignInForm(final UserService userService, final UserDTO userDTO) {
        this.userService = userService;
        emailField = new TextField("E-mail:");
        passwordField = new PasswordField("Password:");
        binder = getBinder();
        binder.readBean(userDTO);
        addComponents(emailField, passwordField, getSignInButton(userDTO));
    }

    private Binder<UserDTO> getBinder() {
        if (binder == null) {
            binder = new Binder<>(UserDTO.class);
            binder.forField(emailField).withValidator(new EmailValidator("Please enter a valid e-mail!")).
                    bind(UserDTO::getEmail, UserDTO::setEmail);
            binder.forField(passwordField).withValidator(password -> password != null && password.length() > 7,
                    "Please enter a password with minimal length of 8 characters!").
                    bind(UserDTO::getPassword, UserDTO::setPassword);
        }
        return binder;
    }

    private Button getSignInButton(final UserDTO userDTO) {
        if (signInButton == null) {
            signInButton = new Button("Sign In");
            signInButton.addClickListener(clickEvent -> {
                if (binder.writeBeanIfValid(userDTO)) {
                    final UserDTO signedInUser = userService.findByEmailAndPassword(userDTO);
                    if (signedInUser == null) {
                        Notification.show("Warning!\n" + "Incorrect E-mail or/and Password!",
                                Notification.Type.WARNING_MESSAGE);
                    } else {
                        Notification.show("User signed in!",
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
        return signInButton;
    }

}
