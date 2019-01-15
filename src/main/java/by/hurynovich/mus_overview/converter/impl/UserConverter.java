package by.hurynovich.mus_overview.converter.impl;

import by.hurynovich.mus_overview.converter.DTOEntityConverter;
import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.entity.impl.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserConverter implements DTOEntityConverter<UserDTO, UserEntity> {
    @Override
    public UserDTO convertToDTO(UserEntity entity) {
        if (entity == null) {
            return null;
        } else {
            final UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(entity, userDTO, "password");
            return userDTO;
        }
    }

    @Override
    public UserEntity convertToEntity(UserDTO dto) {
        final UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(dto, userEntity);
        return userEntity;
    }
}