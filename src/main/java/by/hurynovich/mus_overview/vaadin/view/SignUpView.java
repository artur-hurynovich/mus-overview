package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@SpringView(name = SignUpView.NAME)
public class SignUpView extends CustomComponent implements View {

    public final static String NAME = "signUp";

    @Autowired
    @Qualifier("signUpForm")
    private AbstractDTOForm signUpForm;

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        setCompositionRoot(signUpForm);
    }

}
