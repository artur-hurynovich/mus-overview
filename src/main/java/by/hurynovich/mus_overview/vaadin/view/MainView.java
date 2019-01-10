package by.hurynovich.mus_overview.vaadin.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;

@SpringView(name = MainView.NAME)
public class MainView extends CustomComponent implements View {

    public final static String NAME = "main";

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        final MenuBar menuBar = new MenuBar();
        menuBar.addItem("Groups",
                (MenuBar.Command) selectedItem ->
                        getUI().getNavigator().navigateTo(GroupView.NAME));
        menuBar.addItem("Overviews",
                (MenuBar.Command) selectedItem ->
                        getUI().getNavigator().navigateTo(OverviewView.NAME));
        setCompositionRoot(menuBar);
    }
}
