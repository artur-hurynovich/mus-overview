package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.enumeration.UserRole;
import by.hurynovich.mus_overview.vaadin.util.auth_checker.IAuthChecker;
import by.hurynovich.mus_overview.vaadin.view.impl.UserView;
import by.hurynovich.mus_overview.vaadin.view.impl.OverviewView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.vaadin.spring.security.VaadinSecurity;

@SpringView(name = ProfileView.NAME)
public class ProfileView extends CustomComponent implements View {

    @Autowired
    private VaadinSecurity vaadinSecurity;

    @Autowired
    @Qualifier("authChecker")
    private IAuthChecker authChecker;

    public final static String NAME = "profile";

    private HorizontalLayout parentLayout;

    private Button signUpButton;

    private Button signInButton;

    private Button listUsersButton;

    private Button signOutButton;

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        setCompositionRoot(getParentLayout());
    }

    private HorizontalLayout getParentLayout() {
        if (parentLayout == null) {
            parentLayout = new HorizontalLayout();
            parentLayout.addComponents(getSignUpButton(), getSignInButton(), getListUsersButton(),
                    getSignOutButton());
            parentLayout.setMargin(true);
        }
        return parentLayout;
    }

    private Button getSignUpButton() {
        if (signUpButton == null) {
            signUpButton = new Button("Sign Up");
            signUpButton.addClickListener(clickEvent -> UI.getCurrent().getNavigator().navigateTo(SignUpView.NAME));
        }
        return signUpButton;
    }

    private Button getSignInButton() {
        if (signInButton == null) {
            signInButton = new Button("Sign In");
            signInButton.addClickListener(clickEvent -> UI.getCurrent().getNavigator().navigateTo(SignInView.NAME));
        }
        signInButton.setEnabled(!vaadinSecurity.isAuthenticated());
        return signInButton;
    }

    private Button getListUsersButton() {
        if (listUsersButton == null) {
            listUsersButton = new Button("List Users");
            listUsersButton.addClickListener(clickEvent ->
                    UI.getCurrent().getNavigator().navigateTo(UserView.NAME));
        }
        if (authChecker.checkAuth(UserRole.SUPER_ADMIN, UserRole.ADMIN)) {
            listUsersButton.setVisible(true);
        } else {
            listUsersButton.setVisible(false);
        }
        return listUsersButton;
    }

    private Button getSignOutButton() {
        if (signOutButton == null) {
            signOutButton = new Button("Sign Out");
            signOutButton.addClickListener(clickEvent -> vaadinSecurity.logout());
        }
        signOutButton.setEnabled(vaadinSecurity.isAuthenticated());
        return signOutButton;
    }

}
