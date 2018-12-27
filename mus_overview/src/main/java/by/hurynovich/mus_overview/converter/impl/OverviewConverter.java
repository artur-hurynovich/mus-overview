package by.hurynovich.mus_overview.converter.impl;

import by.hurynovich.mus_overview.converter.DTOEntityConverter;
import by.hurynovich.mus_overview.dto.TagDTO;
import by.hurynovich.mus_overview.entity.OverviewEntity;
import by.hurynovich.mus_overview.dto.OverviewDTO;
import by.hurynovich.mus_overview.entity.TagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OverviewConverter implements DTOEntityConverter<OverviewDTO, OverviewEntity> {
    private final TagConverter tagConverter;

    @Autowired
    public OverviewConverter(final TagConverter tagConverter) {
        this.tagConverter = tagConverter;
    }

    @Override
    public OverviewDTO convertToDTO(final OverviewEntity overviewEntity) {
        final long id = overviewEntity.getId();
        final String title = overviewEntity.getTitle();
        final String text = overviewEntity.getText();
        final LocalDate date = overviewEntity.getDate();
        final long subgroupId = overviewEntity.getSubgroupId();
        final List<TagDTO> tags = tagConverter.convertToDTO(overviewEntity.getTags());
        return new OverviewDTO(id, title, text, date, subgroupId, tags);
    }

    @Override
    public OverviewEntity convertToEntity(final OverviewDTO overviewDto) {
        final long id = overviewDto.getId();
        final String title = overviewDto.getTitle();
        final String text = overviewDto.getText();
        final LocalDate date = overviewDto.getDate();
        final long subgroupId = overviewDto.getSubgroupId();
        final List<TagEntity> tags = tagConverter.convertToEntity(overviewDto.getTags());
        return new OverviewEntity(id, title, text, date, subgroupId, tags);
    }
}
