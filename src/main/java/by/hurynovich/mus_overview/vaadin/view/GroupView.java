package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.dto.GroupDTO;
import by.hurynovich.mus_overview.dto.SubgroupDTO;
import by.hurynovich.mus_overview.service.GroupService;
import by.hurynovich.mus_overview.vaadin.from.GroupForm;
import by.hurynovich.mus_overview.vaadin.from.SubgroupForm;
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

import java.util.List;

@SpringView(name = GroupView.NAME)
public class GroupView extends CustomComponent implements View {

    public final static String NAME = "group";

    private final GroupService groupService;

    private final VerticalLayout parentLayout;

    private final Grid<GroupDTO> groupGrid;

    private final Grid<SubgroupDTO> subgroupGrid;

    private ListDataProvider<GroupDTO> groupDataProvider;

    private ListDataProvider<SubgroupDTO> subgroupDataProvider;

    private final HorizontalLayout groupButtonsLayout;

    private final HorizontalLayout subgroupButtonsLayout;

    private final Button addGroupButton;

    private final Button addSubgroupButton;

    private final Button editGroupButton;

    private final Button editSubgroupButton;

    private final Button removeGroupButton;

    private final Button removeSubgroupButton;

    @Autowired
    public GroupView(final GroupService groupService) {
        this.groupService = groupService;
        parentLayout = new VerticalLayout();
        groupGrid = new Grid<>(GroupDTO.class);
        subgroupGrid = new Grid<>(SubgroupDTO.class);
        groupButtonsLayout = new HorizontalLayout();
        subgroupButtonsLayout = new HorizontalLayout();
        addGroupButton = new Button("Add");
        editGroupButton = new Button("Edit");
        removeGroupButton = new Button("Remove");
        addSubgroupButton = new Button("Add");
        editSubgroupButton = new Button("Edit");
        removeSubgroupButton = new Button("Remove");
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        parentLayout.addComponents(getGroupGrid(), getGroupButtonsLayout(), getSubgroupGrid(),
                getSubgroupButtonsLayout());
        setCompositionRoot(parentLayout);
    }

    private Grid<GroupDTO> getGroupGrid() {
        final List<GroupDTO> allGroups = groupService.getAllGroups();
        groupDataProvider = new ListDataProvider<>(allGroups);
        groupGrid.removeColumn("id");
        groupGrid.setCaption("Groups:");
        groupGrid.setDataProvider(groupDataProvider);
        groupGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        groupGrid.addSelectionListener(selectionEvent -> {
            final int selectedItemsCount = groupGrid.getSelectedItems().size();
            if (selectedItemsCount == 0) {
                editGroupButton.setEnabled(false);
                removeGroupButton.setEnabled(false);
                subgroupGrid.setEnabled(false);
            } else if (selectedItemsCount == 1){
                editGroupButton.setEnabled(true);
                subgroupGrid.setEnabled(true);
                final GroupDTO selectedGroup = selectionEvent.getFirstSelectedItem().orElse(null);
                if (selectedGroup != null) {
                    final long selectedGroupId = selectedGroup.getId();
                    final List<SubgroupDTO> subgroupsByGroupId =
                            groupService.getAllSubgroupsByGroupId(selectedGroupId);
                    subgroupDataProvider = new ListDataProvider<>(subgroupsByGroupId);
                    subgroupGrid.setDataProvider(subgroupDataProvider);
                }
                subgroupGrid.setEnabled(true);
            } else {
                editGroupButton.setEnabled(false);
                subgroupGrid.setEnabled(true);
                subgroupGrid.setEnabled(false);
            }
        });
        return groupGrid;
    }

    private HorizontalLayout getGroupButtonsLayout() {
        groupButtonsLayout.addComponents(getAddGroupButton(), getEditGroupButton(), getRemoveGroupButton());
        return groupButtonsLayout;
    }

    private Button getAddGroupButton() {
        addGroupButton.addClickListener(clickEvent -> {
            final GroupDTO newGroup = new GroupDTO();
            final Window groupWindow = getGroupWindow(newGroup);
            UI.getCurrent().addWindow(groupWindow);
        });
        return addGroupButton;
    }

    private Button getEditGroupButton() {
        editGroupButton.addClickListener(clickEvent -> {
            final GroupDTO groupToUpdate = groupGrid.getSelectedItems().iterator().next();
            final Window groupWindow = getGroupWindow(groupToUpdate);
            UI.getCurrent().addWindow(groupWindow);
        });
        editGroupButton.setEnabled(false);
        return editGroupButton;
    }

    private Button getRemoveGroupButton() {
        removeGroupButton.setEnabled(false);
        return removeGroupButton;
    }

    private Grid<SubgroupDTO> getSubgroupGrid() {
        subgroupGrid.setEnabled(false);
        subgroupGrid.removeColumn("id");
        subgroupGrid.removeColumn("groupId");
        subgroupGrid.setCaption("Subgroups:");
        subgroupGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        subgroupGrid.addSelectionListener(selectionEvent -> {
            final int selectedItemsCount = subgroupGrid.getSelectedItems().size();
            if (selectedItemsCount == 0) {
                editSubgroupButton.setEnabled(false);
                removeSubgroupButton.setEnabled(false);
            } else if (selectedItemsCount == 1){
                editSubgroupButton.setEnabled(true);
                removeSubgroupButton.setEnabled(true);
            } else {
                editSubgroupButton.setEnabled(false);
                removeSubgroupButton.setEnabled(true);
            }
        });
        return subgroupGrid;
    }

    private HorizontalLayout getSubgroupButtonsLayout() {
        subgroupButtonsLayout.addComponents(getAddSubgroupButton(), getEditSubgroupButton(),
                getRemoveSubgroupButton());
        return subgroupButtonsLayout;
    }

    private Button getAddSubgroupButton() {
        addSubgroupButton.addClickListener(clickEvent -> {
            final SubgroupDTO subgroupDTO = new SubgroupDTO();
            final Window subgroupWindow = getSubgroupWindow(subgroupDTO);
            UI.getCurrent().addWindow(subgroupWindow);
        });
        return addSubgroupButton;
    }

    private Button getEditSubgroupButton() {
        editSubgroupButton.addClickListener(clickEvent -> {
            final SubgroupDTO subgroupToUpdate = subgroupGrid.getSelectedItems().iterator().next();
            final Window subgroupWindow = getSubgroupWindow(subgroupToUpdate);
            UI.getCurrent().addWindow(subgroupWindow);
        });
        editSubgroupButton.setEnabled(false);
        return editSubgroupButton;
    }

    private Button getRemoveSubgroupButton() {
        removeSubgroupButton.setEnabled(false);
        return removeSubgroupButton;
    }

    private Window getGroupWindow(final GroupDTO groupDTO) {
        final Window groupWindow = new Window("New Group");
        final GroupForm groupForm = new GroupForm(groupService, groupDTO, groupWindow::close,
                groupWindow::close);
        groupWindow.setContent(groupForm);
        groupWindow.addCloseListener(closeEvent -> groupGrid.getDataProvider().refreshAll());
        return groupWindow;
    }

    private Window getSubgroupWindow(final SubgroupDTO subgroupDTO) {
        final Window subgroupWindow = new Window("New Subgroup");
        final SubgroupForm subgroupForm = new SubgroupForm(groupService, subgroupDTO, subgroupWindow::close,
                subgroupWindow::close);
        subgroupWindow.setContent(subgroupForm);
        subgroupWindow.addCloseListener(closeEvent -> subgroupGrid.getDataProvider().refreshAll());
        return subgroupWindow;
    }

}
