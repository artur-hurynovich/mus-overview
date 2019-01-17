package by.hurynovich.mus_overview.vaadin.custom_field;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.service.IGroupDTOService;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@org.springframework.stereotype.Component("subgroupGroupField")
public class SubgroupGroupField extends CustomField<Long> {

    @Autowired
    @Qualifier("groupService")
    private IGroupDTOService groupService;

    private VerticalLayout parentLayout;

    private ComboBox<GroupDTO> groupField;

    @Override
    protected Component initContent() {
        return getParentLayout();
    }

    @Override
    protected void doSetValue(final Long aLong) {
        if (aLong != null) {
            final GroupDTO groupDTO = groupService.findOne(aLong);
            if (groupDTO != null) {
                getGroupField().setSelectedItem(groupDTO);
            }
        }
    }

    @Override
    public Long getValue() {
        final GroupDTO groupDTO = getGroupField().getSelectedItem().orElse(null);
        if (groupDTO == null) {
            return null;
        } else {
            return groupDTO.getId();
        }
    }

    private VerticalLayout getParentLayout() {
        if (parentLayout == null) {
            parentLayout = new VerticalLayout();
            parentLayout.addComponent(getGroupField());
        }
        return parentLayout;
    }

    private ComboBox<GroupDTO> getGroupField() {
        if (groupField == null) {
            groupField = new ComboBox<>("Group:");
            groupField.setItems(groupService.findAll());
            groupField.setItemCaptionGenerator(GroupDTO::getName);
        }
        return groupField;
    }

}
