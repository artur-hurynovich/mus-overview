package by.hurynovich.mus_overview.converter.impl;

import by.hurynovich.mus_overview.converter.DTOEntityConverter;
import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.entity.impl.SubgroupEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service("subgroupConverter")
public class SubgroupConverter implements DTOEntityConverter<SubgroupDTO, SubgroupEntity> {
    @Override
    public SubgroupDTO convertToDTO(final SubgroupEntity subgroupEntity) {
        if (subgroupEntity == null) {
            return null;
        } else {
            final SubgroupDTO subgroupDto = new SubgroupDTO();
            BeanUtils.copyProperties(subgroupEntity, subgroupDto);
            return subgroupDto;
        }
    }

    @Override
    public SubgroupEntity convertToEntity(final SubgroupDTO subgroupDto) {
        if (subgroupDto == null) {
            return null;
        } else {
            final SubgroupEntity subgroupEntity = new SubgroupEntity();
            BeanUtils.copyProperties(subgroupDto, subgroupEntity);
            return subgroupEntity;
        }
    }
}
