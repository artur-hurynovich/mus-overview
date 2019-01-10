package by.hurynovich.mus_overview.vaadin.ui;

import by.hurynovich.mus_overview.vaadin.view.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI(path = "vaadin")
@Theme("valo")
public class VaadinUI extends UI {

    private final SpringViewProvider viewProvider;

    @Autowired
    public VaadinUI(final SpringViewProvider viewProvider) {
        this.viewProvider = viewProvider;
    }

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        final Navigator navigator = new Navigator(this, this);
        navigator.addProvider(viewProvider);
        navigator.navigateTo(MainView.NAME);
    }
}
