package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.vaadin.view.impl.OverviewView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringView(name = "profileView")
public class ProfileView extends CustomComponent implements View {

    public final static String NAME = "profileView";

    private HorizontalLayout parentLayout;

    private Button signUpButton;

    private Button signInButton;

    private Button signOutButton;

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        setCompositionRoot(getParentLayout());
    }

    private HorizontalLayout getParentLayout() {
        if (parentLayout == null) {
            parentLayout = new HorizontalLayout();
            parentLayout.addComponents(getSignUpButton(), getSignInButton(), getSignOutButton());
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
        return signInButton;
    }

    private Button getSignOutButton() {
        if (signOutButton == null) {
            signOutButton = new Button("Sign Out");
            signOutButton.addClickListener(clickEvent -> {
                SecurityContextHolder.clearContext();
                UI.getCurrent().getNavigator().navigateTo(OverviewView.NAME);
            });
        }
        return signOutButton;
    }

}
