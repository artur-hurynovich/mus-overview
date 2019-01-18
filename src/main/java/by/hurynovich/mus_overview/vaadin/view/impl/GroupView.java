package by.hurynovich.mus_overview.vaadin.view.impl;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.enumeration.UserRole;
import by.hurynovich.mus_overview.service.IGroupDTOService;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import by.hurynovich.mus_overview.vaadin.view.GroupDTOView;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;
import java.util.Set;

@SpringView(name = GroupView.NAME)
@Secured("ADMIN")
public class GroupView extends GroupDTOView {

    public final static String NAME = "group";

    @Autowired
    @Qualifier("groupService")
    private IGroupDTOService groupService;

    @Autowired
    @Qualifier("groupForm")
    private AbstractDTOForm groupForm;

    private CallbackDataProvider<GroupDTO, String> groupDTOGridDataProvider;

    private final Window groupWindow;

    public GroupView() {
        setStartDataProvider(getGroupDTOGridDataProvider());
        groupWindow = new Window("Edit Group");
        groupWindow.addCloseListener(closeEvent -> {
            getGrid().deselectAll();
            getGrid().getDataProvider().refreshAll();
        });
    }

    @PostConstruct
    public void init() {
        setupAddButton();
        setupEditButton();
        setupDeleteButton();
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
            groupForm.setupForm(new GroupDTO(), groupWindow::close, groupWindow::close);
            groupWindow.setContent(groupForm);
            UI.getCurrent().addWindow(groupWindow);
        });
    }

    private void setupEditButton() {
        getEditButton().addClickListener(clickEvent -> {
            final GroupDTO selectedGroupDTO = getGrid().getSelectionModel().getSelectedItems().iterator().next();
            groupForm.setupForm(selectedGroupDTO, groupWindow::close, groupWindow::close);
            groupWindow.setContent(groupForm);
            UI.getCurrent().addWindow(groupWindow);
        });
    }

    private void setupDeleteButton() {
        getDeleteButton().addClickListener(clickEvent -> {
            final Set<GroupDTO> selectedGroups = getGrid().getSelectionModel().getSelectedItems();
            selectedGroups.forEach(groupDTO -> {
                groupService.delete(groupDTO);
            });
            Notification.show("Group(s) deleted!",
                    Notification.Type.ASSISTIVE_NOTIFICATION);
            getGrid().deselectAll();
            getGrid().getDataProvider().refreshAll();
        });
    }

}
