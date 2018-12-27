package by.hurynovich.mus_overview.converter.impl;

import by.hurynovich.mus_overview.converter.DTOEntityConverter;
import by.hurynovich.mus_overview.entity.SubgroupEntity;
import by.hurynovich.mus_overview.dto.SubgroupDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class SubgroupConverter implements DTOEntityConverter<SubgroupDTO, SubgroupEntity> {
    @Override
    public SubgroupDTO convertToDTO(final SubgroupEntity subgroupEntity) {
        final SubgroupDTO subgroupDto = new SubgroupDTO();
        BeanUtils.copyProperties(subgroupEntity, subgroupDto);
        return subgroupDto;
    }

    @Override
    public SubgroupEntity convertToEntity(final SubgroupDTO subgroupDto) {
        final SubgroupEntity subgroupEntity = new SubgroupEntity();
        BeanUtils.copyProperties(subgroupDto, subgroupEntity);
        return subgroupEntity;
    }
}
