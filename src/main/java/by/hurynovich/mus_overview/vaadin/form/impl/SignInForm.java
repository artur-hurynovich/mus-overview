package by.hurynovich.mus_overview.vaadin.form.impl;

import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.service.UserDetailsServiceImpl;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import by.hurynovich.mus_overview.vaadin.view.SignUpView;
import by.hurynovich.mus_overview.vaadin.view.impl.OverviewView;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.vaadin.spring.security.VaadinSecurity;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

@Component("signInForm")
@ViewScope
public class SignInForm extends AbstractDTOForm<UserDTO> {

    @Autowired
    private VaadinSecurity vaadinSecurity;

    @Autowired
    @Qualifier("userService")
    private UserDetailsServiceImpl userService;

    private UserDTO userDTO;

    private TextField emailField;

    private PasswordField passwordField;

    private HorizontalLayout buttonsLayout;

    private Button signInButton;

    private Button signUpButton;

    @PostConstruct
    public void init() {
        userDTO = new UserDTO();
        setupBinder();
        setupParentLayout();
        setContent(getParentLayout());
    }

    private void setupParentLayout() {
        getParentLayout().addComponents(getEmailField(), getPasswordField(), getButtonsLayout());
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

    protected HorizontalLayout getButtonsLayout() {
        if (buttonsLayout == null) {
            buttonsLayout = new HorizontalLayout();
            final Label orLabel = new Label("or");
            buttonsLayout.addComponents(getSignInButton(), orLabel, getSignUpButton());
        }
        return buttonsLayout;
    }

    private Button getSignInButton() {
        if (signInButton == null) {
            signInButton = new Button("Sign In");
            signInButton.addClickListener(clickEvent -> {
                if (getBinder().writeBeanIfValid(userDTO)) {
                    try {
                        vaadinSecurity.login(emailField.getValue(), passwordField.getValue());
                        UI.getCurrent().getNavigator().navigateTo(OverviewView.NAME);
                    } catch (Exception e) {
                        Notification.show("Warning!\nSigning In failed!",
                                Notification.Type.WARNING_MESSAGE);
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

    private Button getSignUpButton() {
        if (signUpButton == null) {
            signUpButton = new Button("Sign Up");
            signUpButton.addClickListener(clickEvent ->
                    UI.getCurrent().getNavigator().navigateTo(SignUpView.NAME));
        }
        return signUpButton;
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
