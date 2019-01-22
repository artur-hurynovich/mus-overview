package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@SpringView(name = SignInView.NAME)
public class SignInView extends CustomComponent implements View {
    public final static String NAME = "signIn";
    private final AbstractDTOForm signInForm;
    private VerticalLayout parentLayout;

    @Autowired
    public SignInView(final @Qualifier("signInForm") AbstractDTOForm signInForm) {
        this.signInForm = signInForm;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        setCompositionRoot(getParentLayout());
    }

    private VerticalLayout getParentLayout() {
        if (parentLayout == null) {
            parentLayout = new VerticalLayout();
            parentLayout.addComponent(signInForm);
        }
        return parentLayout;
    }
}
