package by.hurynovich.mus_overview.vaadin.ui;

import by.hurynovich.mus_overview.vaadin.view.AccessDeniedView;
import by.hurynovich.mus_overview.vaadin.view.ProfileView;
import by.hurynovich.mus_overview.vaadin.view.impl.GroupView;
import by.hurynovich.mus_overview.vaadin.view.impl.OverviewView;
import by.hurynovich.mus_overview.vaadin.view.impl.SubgroupView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.VaadinSecurity;

@SpringUI(path = "vaadin")
@Theme("mytheme")
@SpringViewDisplay
public class VaadinUI extends UI implements ViewDisplay {
    private VaadinSecurity vaadinSecurity;
    private SpringViewProvider viewProvider;
    private VerticalLayout parentLayout;
    private HorizontalLayout menuLayout;
    private Panel viewPanel;

    @Autowired
    public VaadinUI(final VaadinSecurity vaadinSecurity, final SpringViewProvider viewProvider) {
        this.vaadinSecurity = vaadinSecurity;
        this.viewProvider = viewProvider;
    }

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
        getMenuLayout().addComponents(getSecuredMenuButton("Groups", GroupView.NAME),
                getSecuredMenuButton("Subgroups", SubgroupView.NAME),
                getMenuButton("Overviews", OverviewView.NAME),
                getMenuButton("Profile", ProfileView.NAME));
        getParentLayout().addComponents(getMenuLayout(), getViewPanel());
        setContent(getParentLayout());
    }

    @Override
    public void showView(final View view) {
        getViewPanel().setContent((Component) view);
    }

    private VerticalLayout getParentLayout() {
        if (parentLayout == null) {
            parentLayout = new VerticalLayout();
        }
        return parentLayout;
    }

    private HorizontalLayout getMenuLayout() {
        if (menuLayout == null) {
            menuLayout = new HorizontalLayout();
        }
        return menuLayout;
    }

    private Button getMenuButton(final String caption, final String viewName) {
        final Button button = new Button(caption);
        button.addClickListener(clickEvent -> getUI().getNavigator().navigateTo(viewName));
        return button;
    }

    private Button getSecuredMenuButton(final String caption, final String viewName) {
        final Button button = new Button(caption);
        button.addClickListener(clickEvent -> {
            if (vaadinSecurity.isAuthenticated()) {
                getUI().getNavigator().navigateTo(viewName);
            } else {
                getUI().getNavigator().navigateTo(AccessDeniedView.NAME);
            }
        });
        return button;
    }

    private Panel getViewPanel() {
        if (viewPanel == null) {
            viewPanel = new Panel();
        }
        return viewPanel;
    }
}