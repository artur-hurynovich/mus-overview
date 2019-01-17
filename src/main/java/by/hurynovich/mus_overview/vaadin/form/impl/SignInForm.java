package by.hurynovich.mus_overview.vaadin.form.impl;

import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.service.IUserDTOService;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

public class SignInForm extends AbstractDTOForm<UserDTO> {

    @Autowired
    @Qualifier("userService")
    private IUserDTOService userService;

    private UserDTO userDTO;

    private TextField emailField;

    private PasswordField passwordField;

    private Button signInButton;

    @PostConstruct
    public void init() {
        userDTO = new UserDTO();
        setupBinder();
        setupParentLayout();
        setContent(getParentLayout());
    }

    private void setupParentLayout() {
        getParentLayout().addComponents(getEmailField(), getPasswordField(), getSignInButton());
    }

    private TextField getEmailField() {
        if (emailField == null) {
            emailField = new TextField("E-mail:");
        }
        return emailField;
    }

    private PasswordField getPasswordField() {
        if (passwordField == null) {
            passwordField = new PasswordField("Password:");
        }
        return passwordField;
    }

    private Button getSignInButton() {
        if (signInButton == null) {
            signInButton = new Button("Sign In");
            signInButton.addClickListener(clickEvent -> {
                if (getBinder().writeBeanIfValid(userDTO)) {
                    final UserDTO signedInUser = userService.findByEmailAndPassword(userDTO);
                    if (signedInUser == null) {
                        Notification.show("Warning!\nIncorrect E-mail or/and Password!",
                                Notification.Type.WARNING_MESSAGE);
                    } else {
                        Notification.show("User signed in!",
                                Notification.Type.HUMANIZED_MESSAGE);
                    }
                } else {
                    final String validationError = getBinder().validate().getValidationErrors().stream().
                            map(ValidationResult::getErrorMessage).collect(Collectors.joining(";\n"));
                    Notification.show("Warning!\n" + validationError,
                            Notification.Type.WARNING_MESSAGE);
                }
            });
        }
        return signInButton;
    }

    private void setupBinder() {
        getBinder().readBean(userDTO);
        getBinder().forField(getEmailField()).
                withValidator(new EmailValidator("Please, enter a valid e-mail!")).
                bind(UserDTO::getEmail, UserDTO::setEmail);
        getBinder().forField(getPasswordField()).withValidator(password -> password != null && password.length() > 7,
                "Please, enter a password with minimal length of 8 characters!").
                bind(UserDTO::getPassword, UserDTO::setPassword);
    }

    @Override
    public void setupForm(final UserDTO userDTO, final Runnable onSave, final Runnable inDiscard) {
    }
}
