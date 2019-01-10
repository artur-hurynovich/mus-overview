package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.dto.OverviewDTO;
import by.hurynovich.mus_overview.dto.TagDTO;
import by.hurynovich.mus_overview.service.GroupService;
import by.hurynovich.mus_overview.service.OverviewService;
import by.hurynovich.mus_overview.vaadin.from.OverviewForm;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@SpringView(name = OverviewView.NAME)
public class OverviewView extends CustomComponent implements View {

    public final static String NAME = "overview";

    private final OverviewService overviewService;

    private final GroupService groupService;

    private final VerticalLayout parentLayout;

    private ListDataProvider<OverviewDTO> dataProvider;

    private final Grid<OverviewDTO> overviewGrid;

    private final HorizontalLayout buttonsLayout;

    private final Button addButton;

    private final Button editButton;

    private final Button removeButton;

    private OverviewDTO emptyOverview;

    private List<TagDTO> emptyTags;

    @Autowired
    public OverviewView(final OverviewService overviewService, final GroupService groupService) {
        this.overviewService = overviewService;
        this.groupService = groupService;
        parentLayout = new VerticalLayout();
        overviewGrid = new Grid<>(OverviewDTO.class);
        buttonsLayout = new HorizontalLayout();
        addButton = new Button("Add");
        editButton = new Button("Edit");
        removeButton = new Button("Remove");
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        parentLayout.addComponents(getOverviewGrid(), getButtonsLayout());
        setCompositionRoot(parentLayout);
    }

    private Grid<OverviewDTO> getOverviewGrid() {
        final List<OverviewDTO> allOverviews = overviewService.getAllOverviews();
        dataProvider = DataProvider.ofCollection(allOverviews);
        overviewGrid.setDataProvider(dataProvider);
        overviewGrid.setColumnOrder("title", "text", "date", "tags");
        overviewGrid.removeColumn("id");
        overviewGrid.removeColumn("subgroupId");
        return overviewGrid;
    }

    private HorizontalLayout getButtonsLayout() {
        buttonsLayout.addComponents(getAddButton(), getEditButton(), getRemoveButton());
        return buttonsLayout;
    }

    private Button getAddButton() {
        addButton.addClickListener(clickEvent -> {
            final OverviewDTO newOverview = getEmptyOverview();
            final Window overviewWindow = getOverviewWindow(newOverview);
            UI.getCurrent().addWindow(overviewWindow);
        });
        return addButton;
    }

    private Button getEditButton() {
        return editButton;
    }

    private Button getRemoveButton() {
        return removeButton;
    }

    private OverviewDTO getEmptyOverview() {
        if (emptyOverview == null) {
            emptyOverview = new OverviewDTO();
        }
        if (emptyTags == null) {
            emptyTags = new ArrayList<>();
        }
        emptyOverview.setTags(emptyTags);
        return emptyOverview;
    }

    private Window getOverviewWindow(final OverviewDTO overviewDTO) {
        final Window overviewWindow = new Window();
        overviewWindow.setCaption("New Overview");
        final OverviewForm overviewForm = new OverviewForm(overviewService, groupService, overviewDTO
                , () -> {}, () -> {});
        overviewWindow.addCloseListener(closeEvent -> overviewGrid.getDataProvider().refreshAll());
        overviewWindow.setContent(overviewForm);
        return overviewWindow;
    }

}
