package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.dto.impl.UserDTO;

public class UserDTOView extends AbstractDTOView<UserDTO> {

    @Override
    protected Class<UserDTO> getDTOClass() {
        return UserDTO.class;
    }
}
