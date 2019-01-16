package by.hurynovich.mus_overview.vaadin.custom_field;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.service.GroupService;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;

import java.util.List;

public class OverviewSubgroupField extends CustomField<Long> {

    private final GroupService groupService;

    private HorizontalLayout parentLayout;

    private ComboBox<GroupDTO> groupField;

    private ComboBox<SubgroupDTO> subgroupField;

    public OverviewSubgroupField(final GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    protected Component initContent() {
        return getParentLayout();
    }

    @Override
    protected void doSetValue(final Long aLong) {
        if (aLong != null) {
            final SubgroupDTO subgroupDTO = groupService.getSubgroupById(aLong);
            if (subgroupDTO != null) {
                final long groupId = subgroupDTO.getGroupId();
                final GroupDTO groupDTO = groupService.getGroupById(groupId);
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
            final List<GroupDTO> groups = groupService.getAllGroups();
            groupField.setItems(groups);
            groupField.addValueChangeListener(valueChangeEvent -> {
                final GroupDTO groupDTO = valueChangeEvent.getValue();
                if (groupDTO != null) {
                    final long groupId = groupDTO.getId();
                    final List<SubgroupDTO> subgroups = groupService.getAllSubgroupsByGroupId(groupId);
                    getSubgroupField().setItems(subgroups);
                    getSubgroupField().setEnabled(true);
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
