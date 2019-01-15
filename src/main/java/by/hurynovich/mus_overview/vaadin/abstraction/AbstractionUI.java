package by.hurynovich.mus_overview.vaadin.abstraction;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("mytheme")
@SpringViewDisplay
public class AbstractionUI extends UI implements ViewDisplay {

    private VerticalLayout parentLayout;

    private Panel viewPanel;

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        getParentLayout().addComponent(getViewPanel());
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

    private Panel getViewPanel() {
        if (viewPanel == null) {
            viewPanel = new Panel();
        }
        return viewPanel;
    }

}
