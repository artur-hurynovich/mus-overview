package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.dto.OverviewDTO;
import by.hurynovich.mus_overview.dto.TagDTO;
import by.hurynovich.mus_overview.service.GroupService;
import by.hurynovich.mus_overview.service.OverviewService;
import by.hurynovich.mus_overview.service.TagService;
import by.hurynovich.mus_overview.vaadin.form.OverviewForm;
import by.hurynovich.mus_overview.vaadin.renderer.TagRenderer;
import com.vaadin.data.provider.CallbackDataProvider;
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

    private final TagService tagService;

    private VerticalLayout parentLayout;

    private CallbackDataProvider<OverviewDTO, String> dataProvider;

    private Grid<OverviewDTO> overviewGrid;

    private HorizontalLayout buttonsLayout;

    private Button addButton;

    private Button editButton;

    private Button removeButton;

    private final static OverviewDTO EMPTY_OVERVIEW;

    private final static List<TagDTO> EMPTY_TAGS;

    static {
        EMPTY_OVERVIEW = new OverviewDTO();
        EMPTY_TAGS = new ArrayList<>();
        EMPTY_OVERVIEW.setTags(EMPTY_TAGS);
    }

    @Autowired
    public OverviewView(final OverviewService overviewService, final GroupService groupService,
                        final TagService tagService) {
        this.overviewService = overviewService;
        this.groupService = groupService;
        this.tagService = tagService;
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
        dataProvider = new CallbackDataProvider<>(
                query -> overviewService.getAllOverviews().stream(),
                query -> (int) overviewService.overviewCount()
        );
        overviewGrid.setDataProvider(dataProvider);
        overviewGrid.setColumnOrder("title", "text", "date", "tags");
        overviewGrid.getColumn("tags").setRenderer(new TagRenderer());
        overviewGrid.removeColumn("id");
        overviewGrid.removeColumn("subgroupId");
        overviewGrid.setSizeFull();
        return overviewGrid;
    }

    private HorizontalLayout getButtonsLayout() {
        buttonsLayout.addComponents(getAddButton(), getEditButton(), getRemoveButton());
        return buttonsLayout;
    }

    private Button getAddButton() {
        addButton.addClickListener(clickEvent -> {
            final Window overviewWindow = getOverviewWindow(EMPTY_OVERVIEW);
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

    private Window getOverviewWindow(final OverviewDTO overviewDTO) {
        final Window overviewWindow = new Window("New Overview");
        final OverviewForm overviewForm = new OverviewForm(overviewService, groupService, tagService,
                overviewDTO, overviewWindow::close, overviewWindow::close);
        overviewWindow.addCloseListener(closeEvent -> overviewGrid.getDataProvider().refreshAll());
        overviewWindow.setContent(overviewForm);
        return overviewWindow;
    }

}
