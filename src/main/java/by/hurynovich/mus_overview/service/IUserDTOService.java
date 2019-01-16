package by.hurynovich.mus_overview.service;

import by.hurynovich.mus_overview.dto.impl.UserDTO;

public interface IUserDTOService extends IDTOService<UserDTO> {

    UserDTO findByEmailAndPassword(final UserDTO userDTO);

    boolean emailExists(final String email);

}
