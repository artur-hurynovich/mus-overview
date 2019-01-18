package by.hurynovich.mus_overview.vaadin.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.stereotype.Component;

@Component
public class AccessDeniedView extends CustomComponent implements View {

    private final static String MESSAGE = "Access denied! Please, sign in!";

    private VerticalLayout parenLayout;

    private Label messageLabel;

    private Button signInButton;

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        setCompositionRoot(getParentLayout());
    }

    private VerticalLayout getParentLayout() {
        if (parenLayout == null) {
            parenLayout = new VerticalLayout();
            parenLayout.addComponents(getMessageLabel(), getSignInButton());
            parenLayout.setComponentAlignment(getMessageLabel(), Alignment.BOTTOM_CENTER);
            parenLayout.setComponentAlignment(getSignInButton(), Alignment.BOTTOM_CENTER);
        }
        return parenLayout;
    }

    private Label getMessageLabel() {
        if (messageLabel == null) {
            messageLabel = new Label(MESSAGE);
        }
        return messageLabel;
    }

    private Button getSignInButton() {
        if (signInButton == null) {
            signInButton = new Button("Sign In");
            signInButton.addClickListener(clickEvent -> UI.getCurrent().getNavigator().navigateTo(SignInView.NAME));
        }
        return signInButton;
    }

}
