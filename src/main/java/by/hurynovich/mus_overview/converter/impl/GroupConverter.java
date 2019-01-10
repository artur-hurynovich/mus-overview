package by.hurynovich.mus_overview.converter.impl;

import by.hurynovich.mus_overview.converter.DTOEntityConverter;
import by.hurynovich.mus_overview.entity.GroupEntity;
import by.hurynovich.mus_overview.dto.GroupDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class GroupConverter implements DTOEntityConverter<GroupDTO, GroupEntity> {
    @Override
    public GroupDTO convertToDTO(final GroupEntity groupEntity) {
        final GroupDTO groupDto = new GroupDTO();
        BeanUtils.copyProperties(groupEntity, groupDto);
        return groupDto;
    }

    @Override
    public GroupEntity convertToEntity(final GroupDTO groupDto) {
        if (groupDto == null) {
            return null;
        } else {
            final GroupEntity groupEntity = new GroupEntity();
            BeanUtils.copyProperties(groupDto, groupEntity);
            return groupEntity;
        }
    }
}
