package by.hurynovich.mus_overview.vaadin.custom_field;

import by.hurynovich.mus_overview.dto.GroupDTO;
import by.hurynovich.mus_overview.dto.SubgroupDTO;
import by.hurynovich.mus_overview.service.GroupService;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;

import java.util.List;

public class OverviewSubgroupField extends CustomField<Long> {

    private final GroupService groupService;

    private final HorizontalLayout parentLayout;

    private final ComboBox<GroupDTO> groupField;

    private final ComboBox<SubgroupDTO> subgroupField;

    private Long value;

    public OverviewSubgroupField(final GroupService groupService) {
        this.groupService = groupService;
        parentLayout = new HorizontalLayout();
        groupField = new ComboBox<>("Group:");
        subgroupField = new ComboBox<>("Subgroup:");
    }

    @Override
    protected Component initContent() {
        return getParentLayout();
    }

    @Override
    protected void doSetValue(final Long aLong) {
        value = aLong;
        if (aLong != null) {
            final SubgroupDTO subgroupDTO = groupService.getSubgroupById(aLong);
            final long groupId = subgroupDTO.getGroupId();
            final GroupDTO groupDTO = groupService.getGroupById(groupId);
            groupField.setSelectedItem(groupDTO);
            subgroupField.setSelectedItem(subgroupDTO);
            subgroupField.setEnabled(true);
        }
    }

    @Override
    public Long getValue() {
        final SubgroupDTO subgroupDTO = subgroupField.getSelectedItem().orElse(null);
        if (subgroupDTO == null) {
            return null;
        } else {
            return subgroupDTO.getId();
        }
    }

    private HorizontalLayout getParentLayout() {
        parentLayout.addComponents(getGroupField(), getSubgroupField());
        return parentLayout;
    }

    private ComboBox<GroupDTO> getGroupField() {
        final List<GroupDTO> groups = groupService.getAllGroups();
        groupField.setItems(groups);
        groupField.addValueChangeListener(valueChangeEvent -> {
            final GroupDTO groupDTO = valueChangeEvent.getValue();
            if (groupDTO != null) {
                final long groupId = groupDTO.getId();
                final List<SubgroupDTO> subgroups = groupService.getAllSubgroupsByGroupId(groupId);
                subgroupField.setItems(subgroups);
                subgroupField.setEnabled(true);
            } else {
                subgroupField.setEnabled(false);
            }
        });
        return groupField;
    }

    private ComboBox<SubgroupDTO> getSubgroupField() {
        subgroupField.setEnabled(false);
        return subgroupField;
    }
}
