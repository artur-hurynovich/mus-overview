package by.hurynovich.mus_overview.vaadin.view.impl;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.dto.impl.OverviewDTO;
import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.dto.impl.TagDTO;
import by.hurynovich.mus_overview.service.IGroupDTOService;
import by.hurynovich.mus_overview.service.IOverviewDTOService;
import by.hurynovich.mus_overview.service.ISubgroupDTOService;
import by.hurynovich.mus_overview.service.ITagDTOService;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import by.hurynovich.mus_overview.vaadin.view.OverviewDTOView;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringView(name = OverviewView.NAME)
public class OverviewView extends OverviewDTOView {

    public final static String NAME = "";

    @Autowired
    @Qualifier("groupService")
    private IGroupDTOService groupDTOService;

    @Autowired
    @Qualifier("subgroupService")
    private ISubgroupDTOService subgroupDTOService;

    @Autowired
    @Qualifier("overviewService")
    private IOverviewDTOService overviewDTOService;

    @Autowired
    @Qualifier("tagService")
    private ITagDTOService tagDTOService;

    @Autowired
    @Qualifier("overviewForm")
    private AbstractDTOForm overviewForm;

    private HorizontalLayout comboBoxesLayout;

    private ComboBox<GroupDTO> groupDTOComboBox;

    private ComboBox<SubgroupDTO> subgroupDTOComboBox;

    private Button showAllButton;

    private CallbackDataProvider<GroupDTO, String> groupDTOComboBoxDataProvider;

    private ConfigurableFilterDataProvider<SubgroupDTO, Void, Long> subgroupDTOComboBoxDataProvider;

    private ConfigurableFilterDataProvider<OverviewDTO, Void, String> overviewDTOGridDataProvider;

    private ConfigurableFilterDataProvider<OverviewDTO, Void, Long> overviewDTOBySubgroupIdGridDataProvider;

    private final Window overviewWindow;

    public OverviewView() {
        setStartDataProvider(getOverviewDTOGridDataProvider());
        overviewWindow = new Window("Edit Overview");
        overviewWindow.addCloseListener(closeEvent -> {
            getGrid().deselectAll();
            getGrid().getDataProvider().refreshAll();
        });
    }

    @PostConstruct
    public void init() {
        setupAddButton();
        setupEditButton();
        setupDeleteButton();
        getParentLayout().addComponents(getComboBoxesLayout(), getGrid(), getButtonsLayout());
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
            groupDTOComboBox.setDataProvider(getGroupDTOComboBoxDataProvider());
            groupDTOComboBox.getDataProvider().refreshAll();
            groupDTOComboBox.addValueChangeListener(valueChangeEvent -> {
                final GroupDTO selectedGroupDTO = valueChangeEvent.getValue();
                if (selectedGroupDTO == null) {
                    getGrid().setDataProvider(getOverviewDTOGridDataProvider());
                    getGrid().getDataProvider().refreshAll();
                    getSubgroupDTOComboBox().setSelectedItem(null);
                    getSubgroupDTOComboBox().setEnabled(false);
                } else {
                    getSubgroupDTOComboBoxDataProvider().setFilter(selectedGroupDTO.getId());
                    getSubgroupDTOComboBox().setDataProvider(getSubgroupDTOComboBoxDataProvider(), s -> null);
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
                    getOverviewDTOBySubgroupIdGridDataProvider().setFilter(selectedSubgroupDTO.getId());
                    getGrid().setDataProvider(getOverviewDTOBySubgroupIdGridDataProvider());
                    getGrid().getDataProvider().refreshAll();
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

    private void setupAddButton() {
        getAddButton().addClickListener(clickEvent -> {
            final OverviewDTO overviewDTO = new OverviewDTO();
            final List<TagDTO> tagDTOList = new ArrayList<>();
            overviewDTO.setTags(tagDTOList);
            overviewForm.setupForm(overviewDTO, overviewWindow::close, overviewWindow::close);
            overviewWindow.setContent(overviewForm);
            UI.getCurrent().addWindow(overviewWindow);
        });
    }

    private void setupEditButton() {
        getEditButton().addClickListener(clickEvent -> {
            final OverviewDTO selectedOverviewDTO = getGrid().getSelectionModel().getSelectedItems().iterator().next();
            overviewForm.setupForm(selectedOverviewDTO, overviewWindow::close, overviewWindow::close);
            overviewWindow.setContent(overviewForm);
            UI.getCurrent().addWindow(overviewWindow);
        });
    }

    private void setupDeleteButton() {
        getDeleteButton().addClickListener(clickEvent -> {
            final Set<OverviewDTO> selectedOverviews = getGrid().getSelectionModel().getSelectedItems();
            selectedOverviews.forEach(overviewDTO -> {
                overviewDTOService.delete(overviewDTO);
            });
            Notification.show("Overview(s) deleted!",
                    Notification.Type.ASSISTIVE_NOTIFICATION);
            getGrid().deselectAll();
            getGrid().getDataProvider().refreshAll();
        });
    }

    private CallbackDataProvider<GroupDTO, String> getGroupDTOComboBoxDataProvider() {
        if (groupDTOComboBoxDataProvider == null) {
            groupDTOComboBoxDataProvider = new CallbackDataProvider<>(
                    query -> groupDTOService.findAll().stream(),
                    query -> (int) groupDTOService.count()
            );
        }
        return groupDTOComboBoxDataProvider;
    }

    private ConfigurableFilterDataProvider<SubgroupDTO, Void, Long> getSubgroupDTOComboBoxDataProvider() {
        if (subgroupDTOComboBoxDataProvider == null) {
            subgroupDTOComboBoxDataProvider = new CallbackDataProvider<SubgroupDTO, Long>(
                    query -> subgroupDTOService.findAllByGroupId(query.getFilter().orElse(-1L)).stream(),
                    query -> (int) subgroupDTOService.countByGroupId(query.getFilter().orElse(-1L))
            ).withConfigurableFilter();
        }
        return subgroupDTOComboBoxDataProvider;
    }

    private ConfigurableFilterDataProvider<OverviewDTO, Void, String> getOverviewDTOGridDataProvider() {
        if (overviewDTOGridDataProvider == null) {
            overviewDTOGridDataProvider = new CallbackDataProvider<OverviewDTO, String>(
                    query -> overviewDTOService.findAll().stream(),
                    query -> (int) overviewDTOService.count()
            ).withConfigurableFilter();
        }
        return overviewDTOGridDataProvider;
    }

    private ConfigurableFilterDataProvider<OverviewDTO, Void, Long> getOverviewDTOBySubgroupIdGridDataProvider() {
        if (overviewDTOBySubgroupIdGridDataProvider == null) {
            overviewDTOBySubgroupIdGridDataProvider = new CallbackDataProvider<OverviewDTO, Long>(
                    query -> overviewDTOService.findAllBySubgroupId(query.getFilter().orElse(-1L)).stream(),
                    query -> (int) overviewDTOService.countBySubgroupId(query.getFilter().orElse(-1L))
            ).withConfigurableFilter();
        }
        return overviewDTOBySubgroupIdGridDataProvider;
    }

}
