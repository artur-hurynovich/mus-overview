package by.hurynovich.mus_overview.vaadin.form.impl;

import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.enumeration.UserRole;
import by.hurynovich.mus_overview.service.UserDetailsServiceImpl;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import by.hurynovich.mus_overview.vaadin.view.SignInView;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Component("signUpForm")
@ViewScope
public class SignUpForm extends AbstractDTOForm<UserDTO> {

    @Autowired
    @Qualifier("userService")
    private UserDetailsServiceImpl userService;

    private UserDTO userDTO;

    private TextField nameField;

    private TextField emailField;

    private PasswordField passwordField;

    private Button signUpButton;

    @PostConstruct
    public void init() {
        userDTO = new UserDTO();
        setupBinder();
        setupParentLayout();
        setContent(getParentLayout());
    }

    private void setupParentLayout() {
        getParentLayout().addComponents(getNameField(), getEmailField(), getPasswordField(),
                getSignUpButton());
    }

    private TextField getNameField() {
        if (nameField == null) {
            nameField = new TextField("Name:");
        }
        return nameField;
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

    private Button getSignUpButton() {
        if (signUpButton == null) {
            signUpButton = new Button("Sign Up");
            signUpButton.addClickListener(clickEvent -> {
                if (getBinder().writeBeanIfValid(userDTO)) {
                    final String email = userDTO.getEmail();
                    if (userService.emailExists(email)) {
                        Notification.show("Warning!\n" + "User with E-mail \'" + userDTO.getEmail() +
                                        "\' already exists!",
                                Notification.Type.WARNING_MESSAGE);
                    } else {
                        if (userService.count() == 0) {
                            userDTO.setRole(UserRole.SUPER_ADMIN);
                        } else {
                            userDTO.setRole(UserRole.USER);
                        }
                        userService.save(userDTO);
                        UI.getCurrent().getNavigator().navigateTo(SignInView.NAME);
                    }
                } else {
                    final String validationError = getBinder().validate().getValidationErrors().stream().
                            map(ValidationResult::getErrorMessage).collect(Collectors.joining(";\n"));
                    Notification.show("Warning!\n" + validationError,
                            Notification.Type.WARNING_MESSAGE);
                }
            });
        }
        return signUpButton;
    }

    private void setupBinder() {
        getBinder().readBean(new UserDTO());
        getBinder().forField(getNameField()).withValidator(name -> name != null && name.length() > 4,
                "Please, enter a name with minimal length of 5 characters!").
                bind(UserDTO::getName, UserDTO::setName);
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
