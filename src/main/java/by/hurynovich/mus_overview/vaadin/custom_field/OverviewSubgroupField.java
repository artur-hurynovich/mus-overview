package by.hurynovich.mus_overview.vaadin.custom_field;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.service.impl.GroupService;
import by.hurynovich.mus_overview.service.impl.SubgroupService;
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

    @Override
    protected Component initContent() {
        return getParentLayout();
    }

    @Override
    protected void doSetValue(final Long aLong) {
        if (aLong != null) {
            final SubgroupDTO subgroupDTO = subgroupService.findOne(aLong);
            if (subgroupDTO != null) {
                final long groupId = subgroupDTO.getGroupId();
                final GroupDTO groupDTO = groupService.findOne(groupId);
                getGroupField().setSelectedItem(groupDTO);
                getSubgroupField().setSelectedItem(subgroupDTO);
                getSubgroupField().setEnabled(true);
            }
        }
    }

    @Override
    public Long getValue() {
        final SubgroupDTO subgroupDTO = getSubgroupField().getSelectedItem().orElse(null);
        if (subgroupDTO == null) {
            return null;
        } else {
            return subgroupDTO.getId();
        }
    }

    private HorizontalLayout getParentLayout() {
        if (parentLayout == null) {
            parentLayout = new HorizontalLayout();
            parentLayout.addComponents(getGroupField(), getSubgroupField());
        }
        return parentLayout;
    }

    private ComboBox<GroupDTO> getGroupField() {
        if (groupField == null) {
            groupField = new ComboBox<>("Group:");
            final List<GroupDTO> groups = groupService.findAll();
            groupField.setItems(groups);
            groupField.addValueChangeListener(valueChangeEvent -> {
                final GroupDTO groupDTO = valueChangeEvent.getValue();
                if (groupDTO != null) {
                    final long groupId = groupDTO.getId();
                    final List<SubgroupDTO> subgroups = subgroupService.findAllByGroupId(groupId);
                    getSubgroupField().setItems(subgroups);
                    getSubgroupField().setEnabled(true);
                    getSubgroupField().setSelectedItem(null);
                } else {
                    getSubgroupField().setEnabled(false);
                }
            });
            groupField.setItemCaptionGenerator(GroupDTO::getName);
        }
        return groupField;
    }

    private ComboBox<SubgroupDTO> getSubgroupField() {
        if (subgroupField == null) {
            subgroupField = new ComboBox<>("Subgroup:");
            subgroupField.setEnabled(false);
            subgroupField.setItemCaptionGenerator(SubgroupDTO::getName);
        }
        return subgroupField;
    }
}
