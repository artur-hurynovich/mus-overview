package by.hurynovich.mus_overview.converter.impl;

import by.hurynovich.mus_overview.converter.DTOEntityConverter;
import by.hurynovich.mus_overview.entity.impl.OverviewEntity;
import by.hurynovich.mus_overview.dto.impl.OverviewDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OverviewConverter implements DTOEntityConverter<OverviewDTO, OverviewEntity> {
    private final TagConverter tagConverter;

    @Autowired
    public OverviewConverter(final TagConverter tagConverter) {
        this.tagConverter = tagConverter;
    }

    @Override
    public OverviewDTO convertToDTO(final OverviewEntity overviewEntity) {
        if (overviewEntity == null) {
            return null;
        } else {
            final OverviewDTO overviewDTO = new OverviewDTO();
            BeanUtils.copyProperties(overviewEntity, overviewDTO, "tags");
            overviewDTO.setTags(tagConverter.convertToDTO(overviewEntity.getTags()));
            return overviewDTO;
        }
    }

    @Override
    public OverviewEntity convertToEntity(final OverviewDTO overviewDto) {
        if (overviewDto == null) {
            return null;
        } else {
            final OverviewEntity overviewEntity = new OverviewEntity();
            BeanUtils.copyProperties(overviewDto, overviewEntity, "tags");
            overviewEntity.setTags(tagConverter.convertToEntity(overviewDto.getTags()));
            return overviewEntity;
        }
    }
}
