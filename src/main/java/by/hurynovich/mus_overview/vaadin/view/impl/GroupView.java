package by.hurynovich.mus_overview.vaadin.view.impl;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.service.IGroupDTOService;
import by.hurynovich.mus_overview.service.ISubgroupDTOService;
import by.hurynovich.mus_overview.service.impl.GroupService;
import by.hurynovich.mus_overview.service.impl.SubgroupService;
import by.hurynovich.mus_overview.vaadin.form.GroupForm;
import by.hurynovich.mus_overview.vaadin.form.SubgroupForm;
import by.hurynovich.mus_overview.vaadin.view.AbstractDTOView;
import by.hurynovich.mus_overview.vaadin.view.GroupDTOView;
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
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Set;

@SpringView(name = GroupView.NAME)
public class GroupView extends GroupDTOView {

    public final static String NAME = "group";

    @Autowired
    @Qualifier("groupService")
    private IGroupDTOService groupService;

    private CallbackDataProvider<GroupDTO, String> groupDTOGridDataProvider;

    public GroupView() {
        setStartDataProvider(getGroupDTOGridDataProvider());
    }

    private CallbackDataProvider<GroupDTO, String> getGroupDTOGridDataProvider() {
        if (groupDTOGridDataProvider == null) {
            groupDTOGridDataProvider = new CallbackDataProvider<>(
                    query -> groupService.findAll().stream(),
                    query -> (int) groupService.count()
            );
            return groupDTOGridDataProvider;
        }
        return groupDTOGridDataProvider;
    }

    /*private Window getGroupWindow(final GroupDTO groupDTO) {
        final Window groupWindow = new Window("New Group");
        final GroupForm groupForm = new GroupForm(groupService, groupDTO, groupWindow::close,
                groupWindow::close);
        groupWindow.addCloseListener(closeEvent -> {
            groupGrid.deselectAll();
            groupDataProvider.refreshAll();
        });
        groupWindow.setContent(groupForm);
        return groupWindow;
    }*/

}
