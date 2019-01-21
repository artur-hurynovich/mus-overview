package by.hurynovich.mus_overview.vaadin.view.impl;

import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.enumeration.UserRole;
import by.hurynovich.mus_overview.service.UserDetailsServiceImpl;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import by.hurynovich.mus_overview.vaadin.util.auth_checker.IAuthChecker;
import by.hurynovich.mus_overview.vaadin.view.UserDTOView;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.HeaderRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.Set;

@SpringView(name = UserView.NAME)
public class UserView extends UserDTOView {

    public final static String NAME = "user";

    @Autowired
    @Qualifier("userService")
    private UserDetailsServiceImpl userService;

    @Autowired
    @Qualifier("userForm")
    private AbstractDTOForm userForm;

    @Autowired
    @Qualifier("authChecker")
    private IAuthChecker authChecker;

    private ConfigurableFilterDataProvider<UserDTO, Void, String> userDTOGridDataProvider;

    private String filter;

    private final Window userWindow;

    public UserView() {
        setStartDataProvider(getUserDTOGridDataProvider());
        userWindow = new Window("Edit User");
        userWindow.addCloseListener(closeEvent -> {
            getGrid().deselectAll();
            getGrid().getDataProvider().refreshAll();
        });
    }

    @PostConstruct
    public void init() {
        setupEditButton();
        setupDeleteButton();
        setupFilter();
        getButtonsLayout().removeComponent(getAddButton());
        getParentLayout().addComponents(getGrid(), getButtonsLayout());
    }

    private ConfigurableFilterDataProvider<UserDTO, Void, String> getUserDTOGridDataProvider() {
        if (userDTOGridDataProvider == null) {
            userDTOGridDataProvider = new CallbackDataProvider<UserDTO, String>(
                    query -> {
                        final String filter = query.getFilter().orElse(null);
                        if (filter == null || filter.isEmpty()) {
                            return userService.findAll().stream();
                        } else {
                            return userService.findByEmail(filter).stream();
                        }
                    },
                    query -> {
                        final String filter = query.getFilter().orElse(null);
                        if (filter == null || filter.isEmpty()) {
                            return (int) userService.count();
                        } else {
                            return (int) userService.countByEmail(filter);
                        }
                    }
            ).withConfigurableFilter();
        }
        return userDTOGridDataProvider;
    }

    private void setupEditButton() {
        getEditButton().addClickListener(clickEvent -> {
            if (authChecker.checkAuth(UserRole.SUPER_ADMIN)) {
                final UserDTO selectedUserDTO = getGrid().getSelectionModel().getSelectedItems().iterator().next();
                userForm.setupForm(selectedUserDTO, userWindow::close, userWindow::close);
                userWindow.setContent(userForm);
                UI.getCurrent().addWindow(userWindow);
            } else {
                Notification.show("Warning!", "You have no permission for performing this operation!",
                        Notification.Type.WARNING_MESSAGE);
            }
        });
    }

    private void setupDeleteButton() {
        getDeleteButton().addClickListener(clickEvent -> {
            if (authChecker.checkAuth(UserRole.SUPER_ADMIN, UserRole.ADMIN)) {
                final Set<UserDTO> selectedUsers = getGrid().getSelectionModel().getSelectedItems();
                selectedUsers.forEach(overviewDTO -> userService.delete(overviewDTO));
                Notification.show("User(s) deleted!",
                        Notification.Type.ASSISTIVE_NOTIFICATION);
                getGrid().deselectAll();
                getGrid().getDataProvider().refreshAll();
            } else {
                Notification.show("Warning!", "You have no permission for performing this operation!",
                        Notification.Type.WARNING_MESSAGE);
            }
        });
    }

    private void setupFilter() {
        HeaderRow filterRow;
        if (getGrid().getHeaderRow(0) == null) {
            filterRow = getGrid().addHeaderRowAt(0);
        } else {
            filterRow = getGrid().addHeaderRowAt(1);
        }
        filterRow.getCell("email").setComponent(getFilterTextField());
    }

    @SuppressWarnings("unchecked")
    private TextField getFilterTextField() {
        final TextField filterField = new TextField();
        filterField.setCaption("Filter...");
        filterField.addValueChangeListener(valueChangeEvent -> {
            filter = valueChangeEvent.getValue();
            ((ConfigurableFilterDataProvider<UserDTO, Void, String>) getGrid()
                    .getDataProvider()).setFilter(filter);
        });
        return filterField;
    }

}
