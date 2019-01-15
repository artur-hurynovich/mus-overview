package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.service.impl.UserService;
import by.hurynovich.mus_overview.vaadin.form.SignInForm;
import by.hurynovich.mus_overview.vaadin.form.SignUpForm;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = UserView.NAME)
public class UserView extends CustomComponent implements View {

    public final static String NAME = "user";

    private HorizontalLayout parentLayout;

    private SignUpForm signUpForm;

    private SignInForm signInForm;

    private final UserService userService;

    private final static UserDTO EMPTY_USER;

    static {
        EMPTY_USER = new UserDTO();
    }

    @Autowired
    public UserView(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        setCompositionRoot(getParentLayout());
    }

    private HorizontalLayout getParentLayout() {
        if (parentLayout == null) {
            parentLayout = new HorizontalLayout();
            parentLayout.addComponents(getSignUpForm(userService), getSignInForm());
        }
        return parentLayout;
    }

    private VerticalLayout getSignUpForm(final UserService userService) {
        if (signUpForm == null) {
            signUpForm = new SignUpForm(userService, EMPTY_USER);
        }
        return signUpForm;
    }

    private SignInForm getSignInForm() {
        if (signInForm == null) {
            signInForm = new SignInForm(userService, EMPTY_USER);
        }
        return signInForm;
    }

}
