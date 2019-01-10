package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.dto.GroupDTO;
import by.hurynovich.mus_overview.dto.SubgroupDTO;
import by.hurynovich.mus_overview.service.GroupService;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

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
        return groupGrid;
    }

    private HorizontalLayout getGroupButtonsLayout() {
        return groupButtonsLayout;
    }

    private Grid<SubgroupDTO> getSubgroupGrid() {
        return subgroupGrid;
    }

    private HorizontalLayout getSubgroupButtonsLayout() {
        return subgroupButtonsLayout;
    }

}
