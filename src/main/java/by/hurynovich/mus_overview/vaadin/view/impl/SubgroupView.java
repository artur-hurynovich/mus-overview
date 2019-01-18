package by.hurynovich.mus_overview.vaadin.view.impl;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.enumeration.UserRole;
import by.hurynovich.mus_overview.service.IGroupDTOService;
import by.hurynovich.mus_overview.service.ISubgroupDTOService;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import by.hurynovich.mus_overview.vaadin.view.SubgroupDTOView;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.Set;

@SpringView(name = SubgroupView.NAME)
public class SubgroupView extends SubgroupDTOView {

    public final static String NAME = "subgroup";

    @Autowired
    @Qualifier("groupService")
    private IGroupDTOService groupService;

    @Autowired
    @Qualifier("subgroupService")
    private ISubgroupDTOService subgroupService;

    @Autowired
    @Qualifier("subgroupForm")
    private AbstractDTOForm subgroupForm;

    private ComboBox<GroupDTO> groupDTOComboBox;

    private CallbackDataProvider<GroupDTO, String> groupDTOComboBoxDataProvider;

    private ConfigurableFilterDataProvider<SubgroupDTO, Void, Long> subgroupDTOGridDataProvider;

    private final Window subgroupWindow;

    public SubgroupView() {
        setStartDataProvider(getSubgroupDTOGridDataProvider());
        subgroupWindow = new Window("Edit Subgroup");
        subgroupWindow.addCloseListener(closeEvent -> {
            getGrid().deselectAll();
            getGrid().getDataProvider().refreshAll();
        });
    }

    @PostConstruct
    public void init() {
        setupAddButton();
        setupEditButton();
        setupDeleteButton();
        getParentLayout().addComponents(getGroupDTOComboBox(), getGrid(), getButtonsLayout());
    }

    private ComboBox<GroupDTO> getGroupDTOComboBox() {
        if (groupDTOComboBox == null) {
            groupDTOComboBox = new ComboBox<>("Groups:");
            groupDTOComboBox.setDataProvider(getGroupDTOComboBoxDataProvider());
            groupDTOComboBox.getDataProvider().refreshAll();
            groupDTOComboBox.setEmptySelectionCaption("All Groups");
            groupDTOComboBox.setItemCaptionGenerator(GroupDTO::getName);
            groupDTOComboBox.addValueChangeListener(valueChangeEvent -> {
                final GroupDTO selectedGroupDTO = groupDTOComboBox.getSelectedItem().orElse(null);
                if (selectedGroupDTO == null) {
                    getSubgroupDTOGridDataProvider().setFilter(-1L);
                    getGrid().setDataProvider(getSubgroupDTOGridDataProvider());
                    getGrid().getDataProvider().refreshAll();
                } else {
                    getSubgroupDTOGridDataProvider().setFilter(selectedGroupDTO.getId());
                    getGrid().setDataProvider(getSubgroupDTOGridDataProvider());
                    getGrid().getDataProvider().refreshAll();
                }
            });
        }
        return groupDTOComboBox;
    }

    private CallbackDataProvider<GroupDTO, String> getGroupDTOComboBoxDataProvider() {
        if (groupDTOComboBoxDataProvider == null) {
            groupDTOComboBoxDataProvider = new CallbackDataProvider<>(
                    query -> groupService.findAll().stream(),
                    query -> (int) groupService.count()
            );
        }
        return groupDTOComboBoxDataProvider;
    }

    private ConfigurableFilterDataProvider<SubgroupDTO, Void, Long> getSubgroupDTOGridDataProvider() {
        if (subgroupDTOGridDataProvider == null) {
            subgroupDTOGridDataProvider = new CallbackDataProvider<SubgroupDTO, Long>(
                    query -> subgroupService.findAllByGroupId(query.getFilter().orElse(-1L)).stream(),
                    query -> (int) subgroupService.countByGroupId(query.getFilter().orElse(-1L))
            ).withConfigurableFilter();
        }
        return subgroupDTOGridDataProvider;
    }

    private void setupAddButton() {
        getAddButton().addClickListener(clickEvent -> {
            if (checkAuth(UserRole.ADMIN)) {
                subgroupForm.setupForm(new SubgroupDTO(), subgroupWindow::close, subgroupWindow::close);
                subgroupWindow.setContent(subgroupForm);
                UI.getCurrent().addWindow(subgroupWindow);
            } else {
                Notification.show("Warning!", "You have no permission for performing this operation!",
                        Notification.Type.WARNING_MESSAGE);
            }
        });
    }

    private void setupEditButton() {
        getEditButton().addClickListener(clickEvent -> {
            if (checkAuth(UserRole.ADMIN)) {
                final SubgroupDTO selectedSubgroupDTO = getGrid().getSelectionModel().getSelectedItems().iterator().next();
                subgroupForm.setupForm(selectedSubgroupDTO, subgroupWindow::close, subgroupWindow::close);
                subgroupWindow.setContent(subgroupForm);
                UI.getCurrent().addWindow(subgroupWindow);
            } else {
                Notification.show("Warning!", "You have no permission for performing this operation!",
                        Notification.Type.WARNING_MESSAGE);
            }
        });
    }

    private void setupDeleteButton() {
        getDeleteButton().addClickListener(clickEvent -> {
            if (checkAuth(UserRole.ADMIN)) {
                final Set<SubgroupDTO> selectedSubgroups = getGrid().getSelectionModel().getSelectedItems();
                selectedSubgroups.forEach(subgroupDTO -> subgroupService.delete(subgroupDTO));
                Notification.show("Subgroup(s) deleted!",
                        Notification.Type.HUMANIZED_MESSAGE);
                getGrid().deselectAll();
                getGrid().getDataProvider().refreshAll();
            } else {
                Notification.show("Warning!", "You have no permission for performing this operation!",
                        Notification.Type.WARNING_MESSAGE);
            }
        });
    }

}
