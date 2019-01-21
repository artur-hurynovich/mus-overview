package by.hurynovich.mus_overview.vaadin.custom_field;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.service.impl.GroupService;
import by.hurynovich.mus_overview.service.impl.SubgroupService;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.server.SerializableFunction;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@org.springframework.stereotype.Component("overviewSubgroupField")
public class OverviewSubgroupField extends CustomField<Long> {

    @Autowired
    @Qualifier("groupService")
    private GroupService groupService;

    @Autowired
    @Qualifier("subgroupService")
    private SubgroupService subgroupService;

    private HorizontalLayout parentLayout;

    private ComboBox<GroupDTO> groupField;

    private ComboBox<SubgroupDTO> subgroupField;

    private CallbackDataProvider<GroupDTO, String> groupDataProvider;

    private ConfigurableFilterDataProvider<SubgroupDTO, Void, Long> subgroupDataProvider;

    private Long value;

    @Override
    protected Component initContent() {
        return getParentLayout();
    }

    @Override
    protected void doSetValue(final Long subgroupId) {
        value = subgroupId;
        if (getValue() == null || getValue() == 0) {
            getGroupField().setSelectedItem(null);
            getSubgroupField().setEnabled(false);
        } else {
            final SubgroupDTO subgroupDTO = subgroupService.findOne(subgroupId);
            final GroupDTO groupDTO = groupService.findOne(subgroupDTO.getGroupId());
            getSubgroupDataProvider().setFilter(groupDTO.getId());
            getSubgroupDataProvider().refreshAll();
            getGroupField().setSelectedItem(groupDTO);
            getSubgroupField().setSelectedItem(subgroupDTO);
        }
    }

    @Override
    public Long getValue() {
        return value;
    }

    private HorizontalLayout getParentLayout() {
        if (parentLayout == null) {
            parentLayout = new HorizontalLayout();
            parentLayout.addComponents(getGroupField(), getSubgroupField());
            parentLayout.setMargin(false);
        }
        return parentLayout;
    }

    private ComboBox<GroupDTO> getGroupField() {
        if (groupField == null) {
            groupField = new ComboBox<>("Group:");
            groupField.setDataProvider(getGroupDataProvider());
            groupField.getDataProvider().refreshAll();
            groupField.setItemCaptionGenerator(GroupDTO::getName);
            groupField.addValueChangeListener(valueChangeEvent -> {
                if (valueChangeEvent.getValue() == null) {
                    getSubgroupField().setEnabled(false);
                } else {
                    getSubgroupField().clear();
                    getSubgroupDataProvider().setFilter(valueChangeEvent.getValue().getId());
                    getSubgroupField().setDataProvider(getSubgroupDataProvider(), s -> null);
                    getSubgroupDataProvider().refreshAll();
                    getSubgroupField().setEnabled(true);
                }
            });
        }
        return groupField;
    }

    private ComboBox<SubgroupDTO> getSubgroupField() {
        if (subgroupField == null) {
            subgroupField = new ComboBox<>("Subgroup:");
            subgroupField.setEnabled(false);
            subgroupField.setDataProvider(getSubgroupDataProvider(), s -> null);
            subgroupField.getDataProvider().refreshAll();
            subgroupField.setItemCaptionGenerator(SubgroupDTO::getName);
            subgroupField.addValueChangeListener(valueChangeEvent -> {
                if (valueChangeEvent.getValue() == null) {
                    value = null;
                } else {
                    value = valueChangeEvent.getValue().getId();
                }
            });
        }
        return subgroupField;
    }

    private CallbackDataProvider<GroupDTO, String> getGroupDataProvider() {
        if (groupDataProvider == null) {
            groupDataProvider = new CallbackDataProvider<>(
                    query -> groupService.findAll().stream(),
                    query -> (int) groupService.count()
            );
        }
        return groupDataProvider;
    }

    private ConfigurableFilterDataProvider<SubgroupDTO, Void, Long> getSubgroupDataProvider() {
        if (subgroupDataProvider == null) {
            subgroupDataProvider = new CallbackDataProvider<SubgroupDTO, Long>(
                    query -> subgroupService.findAllByGroupId(query.getFilter().orElse(-1L)).stream(),
                    query -> (int) subgroupService.countByGroupId(query.getFilter().orElse(-1L))
            ).withConfigurableFilter();
        }
        return subgroupDataProvider;
    }

}
