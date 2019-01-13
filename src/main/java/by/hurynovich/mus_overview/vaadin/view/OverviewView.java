package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.dto.OverviewDTO;
import by.hurynovich.mus_overview.dto.TagDTO;
import by.hurynovich.mus_overview.exception.OverviewDeletingException;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringView(name = OverviewView.NAME)
public class OverviewView extends CustomComponent implements View {

    public final static String NAME = "overview";

    private final OverviewService overviewService;

    private final GroupService groupService;

    private final TagService tagService;

    private Grid<OverviewDTO> overviewGrid;

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
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        setCompositionRoot(getParentLayout());
    }

    private VerticalLayout getParentLayout() {
        final VerticalLayout parentLayout = new VerticalLayout();
        parentLayout.addComponents(getOverviewGrid(), getButtonsLayout());
        return parentLayout;
    }

    private Grid<OverviewDTO> getOverviewGrid() {
        if (overviewGrid == null) {
            overviewGrid = new Grid<>(OverviewDTO.class);
            overviewGrid.setDataProvider(getDataProvider());
            overviewGrid.setSelectionMode(Grid.SelectionMode.MULTI);
            overviewGrid.addSelectionListener(selectionEvent -> {
                final Set<OverviewDTO> selectedOverviewDTOSet = overviewGrid.getSelectionModel().getSelectedItems();
                if (selectedOverviewDTOSet.size() == 0) {
                    getEditButton().setEnabled(false);
                    getRemoveButton().setEnabled(false);
                } else if (selectedOverviewDTOSet.size() == 1) {
                    getEditButton().setEnabled(true);
                    getRemoveButton().setEnabled(true);
                } else {
                    getEditButton().setEnabled(false);
                    getRemoveButton().setEnabled(true);
                }
            });
            overviewGrid.setColumnOrder("title", "text", "date", "tags");
            overviewGrid.getColumn("tags").setRenderer(new TagRenderer());
            overviewGrid.removeColumn("id");
            overviewGrid.removeColumn("subgroupId");
            overviewGrid.setSizeFull();
        }
        return overviewGrid;
    }

    private CallbackDataProvider<OverviewDTO, String> getDataProvider() {
        return new CallbackDataProvider<>(
                query -> overviewService.getAllOverviews().stream(),
                query -> (int) overviewService.overviewCount()
        );
    }

    private HorizontalLayout getButtonsLayout() {
        final HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.addComponents(getAddButton(), getEditButton(), getRemoveButton());
        return buttonsLayout;
    }

    private Button getAddButton() {
        if (addButton == null) {
            addButton = new Button("Add");
            addButton.addClickListener(clickEvent ->
                    UI.getCurrent().addWindow(getOverviewWindow(EMPTY_OVERVIEW)));
        }
        return addButton;
    }

    private Button getEditButton() {
        if (editButton == null) {
            editButton = new Button("Edit");
            editButton.addClickListener(clickEvent -> {
                final OverviewDTO selectedOverviewDTO =
                        overviewGrid.getSelectionModel().getSelectedItems().iterator().next();
                UI.getCurrent().addWindow(getOverviewWindow(selectedOverviewDTO));
            });
            editButton.setEnabled(false);
        }
        return editButton;
    }

    private Button getRemoveButton() {
        if (removeButton == null) {
            removeButton = new Button("Remove");
            removeButton.addClickListener(clickEvent -> {
                final Set<OverviewDTO> selectedOverviewDTOSet =
                        overviewGrid.getSelectionModel().getSelectedItems();
                selectedOverviewDTOSet.forEach(overviewDTO -> {
                    try {
                        overviewService.deleteOverview(overviewDTO.getId());
                    } catch (OverviewDeletingException e) {
                        Notification.show("Error!", "Overview(s) deleting failed!",
                                Notification.Type.ERROR_MESSAGE);
                    }
                });
                overviewGrid.getDataProvider().refreshAll();
            });
            removeButton.setEnabled(false);
        }
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
