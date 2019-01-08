package by.hurynovich.mus_overview.vaadin.ui;

import by.hurynovich.mus_overview.dto.OverviewDTO;
import by.hurynovich.mus_overview.vaadin.form.OverviewForm;
import by.hurynovich.mus_overview.vaadin.util.OverviewDataProvider;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringUI
@Theme("valo")
public class OverviewUI extends UI {

    private final OverviewDataProvider dataProvider;

    private final VerticalLayout parentLayout;

    private final HorizontalLayout buttonsLayout;

    private final Grid<OverviewDTO> overviewGrid;

    private final Button addButton;

    private final Button editButton;

    private final Button deleteButton;

    @Autowired
    public OverviewUI(final OverviewDataProvider dataProvider) {
        this.dataProvider = dataProvider;
        parentLayout = new VerticalLayout();
        buttonsLayout = new HorizontalLayout();
        overviewGrid = new Grid<>();
        addButton = new Button("Add");
        editButton = new Button("Edit");
        deleteButton = new Button("Delete");
    }

    @Override
    protected void init(final VaadinRequest vaadinRequest) {
        setContent(getParentLayout());
    }

    private VerticalLayout getParentLayout() {
        parentLayout.addComponents(getOverviewGrid(), getButtonsLayout());
        return parentLayout;
    }

    private Grid getOverviewGrid() {
        overviewGrid.setDataProvider(dataProvider);
        return overviewGrid;
    }

    private HorizontalLayout getButtonsLayout() {
        buttonsLayout.addComponent(getAddButton());
        return buttonsLayout;
    }

    private Button getAddButton() {
        addButton.addClickListener(clickEvent -> {
            final OverviewDTO overviewDTO = new OverviewDTO();
            overviewDTO.setDate(LocalDate.now());
            overviewDTO.setTags(new ArrayList<>());
            final Window addOverviewWindow = new Window();
            final OverviewForm overviewForm = getOverviewForm(overviewDTO, addOverviewWindow);
            addOverviewWindow.setCaption("Add New Overview");
            addOverviewWindow.setContent(overviewForm);
            addOverviewWindow.addCloseListener(closeEvent -> overviewGrid.getDataProvider().refreshAll());
            addWindow(addOverviewWindow);
        });
        return addButton;
    }

    private OverviewForm getOverviewForm(final OverviewDTO overviewDTO, final Window overviewWindow) {
        return new OverviewForm(overviewDTO, () -> {
            dataProvider.refreshAll();
            overviewWindow.close();
        });
    }

}
