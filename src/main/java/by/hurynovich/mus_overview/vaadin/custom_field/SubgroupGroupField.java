package by.hurynovich.mus_overview.vaadin.custom_field;

import by.hurynovich.mus_overview.dto.GroupDTO;
import by.hurynovich.mus_overview.service.GroupService;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

public class SubgroupGroupField extends CustomField<Long> {

    private final GroupService groupService;

    private final VerticalLayout parentLayout;

    private final ComboBox<GroupDTO> groupField;

    public SubgroupGroupField(final GroupService groupService) {
        this.groupService = groupService;
        parentLayout = new VerticalLayout();
        groupField = new ComboBox<>("Group:");
    }

    @Override
    protected Component initContent() {
        return getParentLayout();
    }

    @Override
    protected void doSetValue(final Long aLong) {
        if (aLong != null) {
            final GroupDTO groupDTO = groupService.getGroupById(aLong);
            if (groupDTO != null) {
                groupField.setSelectedItem(groupDTO);
            }
        }
    }

    @Override
    public Long getValue() {
        final GroupDTO groupDTO = groupField.getSelectedItem().orElse(null);
        if (groupDTO == null) {
            return null;
        } else {
            return groupDTO.getId();
        }
    }

    private VerticalLayout getParentLayout() {
        parentLayout.addComponent(getGroupField());
        return parentLayout;
    }

    private ComboBox<GroupDTO> getGroupField() {
        final List<GroupDTO> groups = groupService.getAllGroups();
        groupField.setItems(groups);
        groupField.setItemCaptionGenerator(GroupDTO::getName);
        return groupField;
    }

}
