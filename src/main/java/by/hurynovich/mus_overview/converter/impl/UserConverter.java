package by.hurynovich.mus_overview.converter.impl;

import by.hurynovich.mus_overview.converter.DTOEntityConverter;
import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.entity.impl.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserConverter implements DTOEntityConverter<UserDTO, UserEntity> {
    @Override
    public UserDTO convertToDTO(final UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        } else {
            final UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userEntity, userDTO);
            return userDTO;
        }
    }

    @Override
    public UserEntity convertToEntity(final UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            final UserEntity userEntity = new UserEntity();
            BeanUtils.copyProperties(userDTO, userEntity);
            return userEntity;
        }
    }
}