package by.hurynovich.mus_overview.vaadin.view.impl;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.dto.impl.OverviewDTO;
import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.dto.impl.TagDTO;
import by.hurynovich.mus_overview.service.impl.GroupService;
import by.hurynovich.mus_overview.service.impl.OverviewService;
import by.hurynovich.mus_overview.service.impl.SubgroupService;
import by.hurynovich.mus_overview.service.impl.TagService;
import by.hurynovich.mus_overview.vaadin.form.OverviewForm;
import by.hurynovich.mus_overview.vaadin.renderer.TagRenderer;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.HeaderRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringView(name = OverviewView.NAME)
public class OverviewView extends CustomComponent implements View {

    public final static String NAME = "";

    private final GroupService groupService;

    private final SubgroupService subgroupService;

    private final OverviewService overviewService;

    private final TagService tagService;

    private HorizontalLayout comboBoxesLayout;

    private ComboBox<GroupDTO> groupDTOComboBox;

    private ComboBox<SubgroupDTO> subgroupDTOComboBox;

    private Button showAllButton;

    private Grid<OverviewDTO> overviewGrid;

    private ConfigurableFilterDataProvider<OverviewDTO, Void, String> allOverviewsDataProvider;

    private ConfigurableFilterDataProvider<OverviewDTO, Void, String> allOverviewsBySubgroupIdDataProvider;

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
    public OverviewView(final @Qualifier("groupService") GroupService groupService,
                        final @Qualifier("subgroupService") SubgroupService subgroupService,
                        final @Qualifier("overviewService") OverviewService overviewService,
                        final @Qualifier("tagService") TagService tagService,
                        final @Qualifier("grid") Grid<OverviewDTO> overviewGrid) {
        this.groupService = groupService;
        this.subgroupService = subgroupService;
        this.overviewService = overviewService;
        this.tagService = tagService;
        this.overviewGrid = overviewGrid;
        initOverviewGrid();
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        setCompositionRoot(getParentLayout());
    }

    private VerticalLayout getParentLayout() {
        final VerticalLayout parentLayout = new VerticalLayout();
        parentLayout.addComponents(getComboBoxesLayout(), getOverviewGrid(), getButtonsLayout());
        return parentLayout;
    }

    private HorizontalLayout getComboBoxesLayout() {
        if (comboBoxesLayout == null) {
            comboBoxesLayout = new HorizontalLayout();
            comboBoxesLayout.addComponents(getGroupDTOComboBox(), getSubgroupDTOComboBox(), getShowAllButton());
            comboBoxesLayout.setComponentAlignment(getShowAllButton(), Alignment.BOTTOM_CENTER);
        }
        return comboBoxesLayout;
    }

    private ComboBox<GroupDTO> getGroupDTOComboBox() {
        if (groupDTOComboBox == null) {
            groupDTOComboBox = new ComboBox<>();
            groupDTOComboBox.setCaption("Groups:");
            groupDTOComboBox.setItemCaptionGenerator(GroupDTO::getName);
            groupDTOComboBox.setEmptySelectionCaption("Chose a Group...");
            groupDTOComboBox.setDataProvider(DataProvider.ofCollection(groupService.findAll()));
            groupDTOComboBox.getDataProvider().refreshAll();
            groupDTOComboBox.addValueChangeListener(valueChangeEvent -> {
                final GroupDTO selectedGroupDTO = valueChangeEvent.getValue();
                if (selectedGroupDTO == null) {
                    getOverviewGrid().setDataProvider(getAllOverviewsDataProvider());
                    getOverviewGrid().getDataProvider().refreshAll();
                    getSubgroupDTOComboBox().setSelectedItem(null);
                    getSubgroupDTOComboBox().setEnabled(false);
                } else {
                    getSubgroupDTOComboBox().setDataProvider(
                            DataProvider.ofCollection(subgroupService.findAllByGroupId(selectedGroupDTO.getId())));
                    getSubgroupDTOComboBox().getDataProvider().refreshAll();
                    getSubgroupDTOComboBox().setSelectedItem(null);
                    getSubgroupDTOComboBox().setEnabled(true);
                }
            });
        }
        return groupDTOComboBox;
    }

    private ComboBox<SubgroupDTO> getSubgroupDTOComboBox() {
        if (subgroupDTOComboBox == null) {
            subgroupDTOComboBox = new ComboBox<>();
            subgroupDTOComboBox.setCaption("Subgroups:");
            subgroupDTOComboBox.setItemCaptionGenerator(SubgroupDTO::getName);
            subgroupDTOComboBox.setEmptySelectionCaption("Choose a Subgroup...");
            subgroupDTOComboBox.setEnabled(false);
            subgroupDTOComboBox.addValueChangeListener(valueChangeEvent -> {
                final SubgroupDTO selectedSubgroupDTO = valueChangeEvent.getValue();
                if (selectedSubgroupDTO != null) {
                    getOverviewGrid().setDataProvider(getAllOverviewsBySubgroupIdDataProvider(subgroupDTOComboBox));
                    getOverviewGrid().getDataProvider().refreshAll();
                }
            });
        }
        return subgroupDTOComboBox;
    }

    private Button getShowAllButton() {
        if (showAllButton == null) {
            showAllButton = new Button("Show All");
            showAllButton.addClickListener(clickEvent -> getGroupDTOComboBox().setSelectedItem(null));
        }
        return showAllButton;
    }

    private void initOverviewGrid() {
        overviewGrid.setDataProvider(getAllOverviewsDataProvider());
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
        final HeaderRow filterRow = overviewGrid.appendHeaderRow();
        filterRow.getCell("tags").setComponent(getTagFilterField());
        overviewGrid.getColumn("tags").setRenderer(new TagRenderer());
    }

    private Grid<OverviewDTO> getOverviewGrid() {
        return overviewGrid;
    }

    private ConfigurableFilterDataProvider<OverviewDTO, Void, String> getAllOverviewsDataProvider() {
        if (allOverviewsDataProvider == null) {
            allOverviewsDataProvider = new CallbackDataProvider<OverviewDTO, String>(
                    query -> overviewService.findAllByTag(query.getFilter().orElse(null)).stream(),
                    query -> (int) overviewService.countByTag(query.getFilter().orElse(null))
            ).withConfigurableFilter();
        }
        return allOverviewsDataProvider;
    }

    private ConfigurableFilterDataProvider<OverviewDTO, Void, String>
    getAllOverviewsBySubgroupIdDataProvider(final ComboBox<SubgroupDTO> subgroupDTOComboBox) {
        if (allOverviewsBySubgroupIdDataProvider == null) {
            allOverviewsBySubgroupIdDataProvider = new CallbackDataProvider<OverviewDTO, String>(
                    query -> overviewService.findAllBySubgroupIdAndTag(
                            subgroupDTOComboBox.getValue().getId(), query.getFilter().orElse(null)).stream(),
                    query -> (int) overviewService.countBySubgroupIdAndTag(
                            subgroupDTOComboBox.getValue().getId(), query.getFilter().orElse(null))
            ).withConfigurableFilter();
        }
        return allOverviewsBySubgroupIdDataProvider;
    }

    @SuppressWarnings("unchecked")
    private TextField getTagFilterField() {
        final TextField tagFilterField = new TextField();
        tagFilterField.setPlaceholder("Filter by tag...");
        tagFilterField.addValueChangeListener(valueChangeEvent ->
                ((ConfigurableFilterDataProvider<OverviewDTO, Void, String>) getOverviewGrid()
                    .getDataProvider()).setFilter(valueChangeEvent.getValue()));
        return tagFilterField;
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
                    overviewService.delete(overviewDTO);
                    Notification.show("Overview \'" + overviewDTO.getName() + "\' deleted!",
                            Notification.Type.ASSISTIVE_NOTIFICATION);
                });
                overviewGrid.deselectAll();
                overviewGrid.getDataProvider().refreshAll();
            });
            removeButton.setEnabled(false);
        }
        return removeButton;
    }

    private Window getOverviewWindow(final OverviewDTO overviewDTO) {
        final Window overviewWindow = new Window("New Overview");
        final OverviewForm overviewForm = new OverviewForm(groupService, subgroupService, overviewService, tagService,
                overviewDTO, overviewWindow::close, overviewWindow::close);
        overviewWindow.addCloseListener(closeEvent -> {
            overviewGrid.deselectAll();
            overviewGrid.getDataProvider().refreshAll();
        });
        overviewWindow.setContent(overviewForm);
        return overviewWindow;
    }

}
