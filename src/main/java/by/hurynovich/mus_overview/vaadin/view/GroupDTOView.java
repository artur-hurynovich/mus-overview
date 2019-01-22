package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;

public class GroupDTOView extends AbstractDTOView<GroupDTO> {
    @Override
    protected Class<GroupDTO> getDTOClass() {
        return GroupDTO.class;
    }
}
