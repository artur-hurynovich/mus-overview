package by.hurynovich.mus_overview.vaadin.view.impl;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.service.IGroupDTOService;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import by.hurynovich.mus_overview.vaadin.view.GroupDTOView;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;

@SpringView(name = GroupView.NAME)
public class GroupView extends GroupDTOView {

    public final static String NAME = "group";

    @Autowired
    @Qualifier("groupService")
    private IGroupDTOService groupService;

    @Autowired
    @Qualifier("groupForm")
    private AbstractDTOForm groupForm;

    private CallbackDataProvider<GroupDTO, String> groupDTOGridDataProvider;

    public GroupView() {
        setStartDataProvider(getGroupDTOGridDataProvider());
    }

    @PostConstruct
    public void init() {
        setupAddButton();
        getParentLayout().addComponents(getGrid(), getButtonsLayout());
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

    private void setupAddButton() {
        getAddButton().addClickListener(clickEvent -> {
            final Window groupWindow = new Window("New Group");
            groupWindow.setContent(groupForm);
            UI.getCurrent().addWindow(groupWindow);
        });
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
