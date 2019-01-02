package by.hurynovich.mus_overview.vaadin;

import by.hurynovich.mus_overview.dto.GroupDTO;
import by.hurynovich.mus_overview.dto.OverviewDTO;
import by.hurynovich.mus_overview.dto.SubgroupDTO;
import by.hurynovich.mus_overview.service.GroupService;
import by.hurynovich.mus_overview.service.OverviewService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "overviews")
public class MainView extends VerticalLayout {
    private final GroupService groupService;
    private final OverviewService overviewService;
    private ComboBox<GroupDTO> groupsComboBox;
    private ComboBox<SubgroupDTO> subgroupsComboBox;
    private final Grid<OverviewDTO> overviewsGrid;
    private final Grid<OverviewDTO> searchGrid;
    private Dialog chooseSubgroupDialog;
    private final static List<OverviewDTO> EMPTY_OVERVIEWS = new ArrayList<>(1);

    @Autowired
    public MainView(final GroupService groupService, final OverviewService overviewService) {
        this.groupService = groupService;
        this.overviewService = overviewService;

        chooseSubgroupDialog = buildChooseSubgroupDialog();
        overviewsGrid = buildShowGrid();
        searchGrid = buildShowGrid();

        final Tab overviewsTab = buildOverviewsTab();
        final Tab searchTab = buildSearchTab();
        final VerticalLayout overviewsLayout = buildOverviewsLayout();
        final VerticalLayout searchLayout = buildSearchLayout();
        final Tabs tabs = new Tabs(overviewsTab, searchTab);

        final Map<Tab, VerticalLayout> tabsToPages = new HashMap<>();
        tabsToPages.put(overviewsTab, overviewsLayout);
        tabsToPages.put(searchTab, searchLayout);

        tabs.addSelectedChangeListener(selectedChangeEvent -> {
            tabsToPages.values().forEach(div -> div.setVisible(false));
            tabsToPages.get(tabs.getSelectedTab()).setVisible(true);
        });

        add(tabs, overviewsLayout, searchLayout);
    }

    private Tab buildOverviewsTab() {
        return new Tab("Overviews");
    }

    private Tab buildSearchTab() {
        return new Tab("Search");
    }

    private VerticalLayout buildOverviewsLayout() {
        final VerticalLayout overviewsLayout = new VerticalLayout();
        final HorizontalLayout showParametersLayout = new HorizontalLayout();
        showParametersLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        final ComboBox<GroupDTO> groupsComboBox = buildGroupsComboBox();
        final ComboBox<SubgroupDTO> subgroupsComboBox = buildSubgroupsComboBox();
        final Button showOverviewsButton = buildShowOverviewsButton();
        showParametersLayout.add(groupsComboBox, subgroupsComboBox, showOverviewsButton);
        overviewsLayout.add(showParametersLayout, overviewsGrid);
        return overviewsLayout;
    }

    private VerticalLayout buildSearchLayout() {
        final VerticalLayout searchLayout = new VerticalLayout();
        final TextField searchTextField = buildSearchTextField();
        searchLayout.add(searchTextField, searchGrid);
        searchLayout.setVisible(false);
        return searchLayout;
    }

    private ComboBox<GroupDTO> buildGroupsComboBox() {
        groupsComboBox = new ComboBox<>();
        groupsComboBox.setLabel("Groups");
        groupsComboBox.setItemLabelGenerator(GroupDTO::getName);
        final List<GroupDTO> groups = groupService.getAllGroups();
        groupsComboBox.setItems(groups);

        groupsComboBox.addValueChangeListener(e -> {
            final GroupDTO selectedGroup = groupsComboBox.getValue();
            if (selectedGroup == null) {
                subgroupsComboBox.setEnabled(false);
            } else {
                subgroupsComboBox.setEnabled(true);
                final long groupId = selectedGroup.getId();
                final List<SubgroupDTO> subgroupsByGroupId = groupService.getAllSubgroupsByGroupId(groupId);
                subgroupsComboBox.setItems(subgroupsByGroupId);
            }
        });

        return groupsComboBox;
    }

    private ComboBox<SubgroupDTO> buildSubgroupsComboBox() {
        subgroupsComboBox = new ComboBox<>();
        subgroupsComboBox.setLabel("Subgroups");
        subgroupsComboBox.setItemLabelGenerator(SubgroupDTO::getName);
        subgroupsComboBox.setEnabled(false);
        return subgroupsComboBox;
    }

    private TextField buildSearchTextField() {
        final TextField tagsTextField = new TextField();
        tagsTextField.setLabel("Tags");
        tagsTextField.setPlaceholder("Tag (or tags, separated by spaces)");
        tagsTextField.addValueChangeListener(e -> {
            final String searchField = tagsTextField.getValue();
            if (!searchField.isEmpty()) {
                final String[] tags = searchField.split(" ");
                final List<OverviewDTO> overviewsByTags = overviewService.getAllOverviewsByTags(tags);
                searchGrid.setItems(overviewsByTags);
            } else {
                searchGrid.setItems(EMPTY_OVERVIEWS);
            }
        });
        return tagsTextField;
    }

    private Button buildShowOverviewsButton() {
        final Button showOverviewsButton = new Button("Show Overviews");
        showOverviewsButton.addClickListener(e -> {
            if (groupsComboBox.getValue() == null) {
                final List<OverviewDTO> allOverviews = overviewService.getAllOverviews();
                overviewsGrid.setItems(allOverviews);
            } else if (subgroupsComboBox.getValue() == null) {
                chooseSubgroupDialog.open();
            } else {
                final long subgroupId = subgroupsComboBox.getValue().getId();
                final List<OverviewDTO> overviewsBySubgroupId =
                        overviewService.getAllOverviewsBySubgroupId(subgroupId);
                if (overviewsBySubgroupId.isEmpty()) {
                    overviewsGrid.setItems(EMPTY_OVERVIEWS);
                } else {
                    overviewsGrid.setItems(overviewsBySubgroupId);
                }
            }
        });
        return showOverviewsButton;
    }

    private Grid<OverviewDTO> buildShowGrid() {
        final Grid<OverviewDTO> showGrid = new Grid<>();
        showGrid.addColumn(OverviewDTO::getTitle).setHeader("Title");
        showGrid.addColumn(OverviewDTO::getText).setHeader("Text");
        showGrid.addColumn(OverviewDTO::getDate).setHeader("Date");
        showGrid.setHeight("300");
        return showGrid;
    }

    private Dialog buildChooseSubgroupDialog() {
        chooseSubgroupDialog = new Dialog();
        chooseSubgroupDialog.setCloseOnEsc(true);
        chooseSubgroupDialog.setCloseOnOutsideClick(true);
        final Label dialogText = new Label("Please, choose a subgroup");
        final Button okButton = new Button("Got it", e -> chooseSubgroupDialog.close());
        final VerticalLayout layout = new VerticalLayout();
        layout.add(dialogText, okButton);
        layout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, dialogText, okButton);
        chooseSubgroupDialog.add(layout);
        return chooseSubgroupDialog;
    }
}
