package by.hurynovich.mus_overview.vaadin.view.impl;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.service.IGroupDTOService;
import by.hurynovich.mus_overview.service.ISubgroupDTOService;
import by.hurynovich.mus_overview.vaadin.view.SubgroupDTOView;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@SpringView(name = SubgroupView.NAME)
public class SubgroupView extends SubgroupDTOView {

    public final static String NAME = "subgroup";

    @Autowired
    @Qualifier("groupService")
    private IGroupDTOService groupDTOService;

    @Autowired
    @Qualifier("subgroupService")
    private ISubgroupDTOService subgroupDTOService;

    private ComboBox<GroupDTO> groupDTOComboBox;

    private CallbackDataProvider<GroupDTO, String> groupDTOComboBoxDataProvider;

    private ConfigurableFilterDataProvider<SubgroupDTO, Void, Long> subgroupDTOGridDataProvider;

    public SubgroupView() {
        getParentLayout().addComponent(getGroupDTOComboBox());
        setStartDataProvider(getSubgroupDTOGridDataProvider());
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
                    query -> groupDTOService.findAll().stream(),
                    query -> (int) groupDTOService.count()
            );
        }
        return groupDTOComboBoxDataProvider;
    }

    private ConfigurableFilterDataProvider<SubgroupDTO, Void, Long> getSubgroupDTOGridDataProvider() {
        if (subgroupDTOGridDataProvider == null) {
            subgroupDTOGridDataProvider = new CallbackDataProvider<SubgroupDTO, Long>(
                    query -> subgroupDTOService.findAllByGroupId(query.getFilter().orElse(-1L)).stream(),
                    query -> (int) subgroupDTOService.countByGroupId(query.getFilter().orElse(-1L))
            ).withConfigurableFilter();
        }
        return subgroupDTOGridDataProvider;
    }

}
