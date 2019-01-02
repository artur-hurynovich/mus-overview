package by.hurynovich.mus_overview.converter.impl;

import by.hurynovich.mus_overview.converter.DTOEntityConverter;
import by.hurynovich.mus_overview.dto.TagDTO;
import by.hurynovich.mus_overview.entity.TagEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagConverter implements DTOEntityConverter<TagDTO, TagEntity> {
    @Override
    public TagDTO convertToDTO(final TagEntity tagEntity) {
        final TagDTO tagDto = new TagDTO();
        final long id = tagEntity.getId();
        final String name = tagEntity.getName();
        tagDto.setId(id);
        tagDto.setName(name);
        return tagDto;
    }

    @Override
    public TagEntity convertToEntity(final TagDTO tagDto) {
        final TagEntity tagEntity = new TagEntity();
        final long id = tagDto.getId();
        final String name = tagDto.getName();
        tagEntity.setId(id);
        tagEntity.setName(name);
        return tagEntity;
    }

    public List<TagDTO> convertToDTO(final List<TagEntity> tagEntities) {
        final List<TagDTO> tagDtos = new ArrayList<>();
        tagEntities.forEach(tagEntity -> tagDtos.add(convertToDTO(tagEntity)));
        return tagDtos;
    }

    public List<TagEntity> convertToEntity(final List<TagDTO> tagDtos) {
        final List<TagEntity> tagEntities = new ArrayList<>();
        tagDtos.forEach(tagDto -> tagEntities.add(convertToEntity(tagDto)));
        return tagEntities;
    }
}
