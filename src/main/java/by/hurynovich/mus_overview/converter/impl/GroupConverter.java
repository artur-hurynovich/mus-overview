package by.hurynovich.mus_overview.converter.impl;

import by.hurynovich.mus_overview.converter.DTOEntityConverter;
import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.entity.impl.GroupEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service("groupConverter")
public class GroupConverter implements DTOEntityConverter<GroupDTO, GroupEntity> {
    @Override
    public GroupDTO convertToDTO(final GroupEntity groupEntity) {
        if (groupEntity == null) {
            return null;
        } else {
            final GroupDTO groupDto = new GroupDTO();
            BeanUtils.copyProperties(groupEntity, groupDto);
            return groupDto;
        }
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
