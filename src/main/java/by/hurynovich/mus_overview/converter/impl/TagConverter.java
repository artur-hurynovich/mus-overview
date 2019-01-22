package by.hurynovich.mus_overview.converter.impl;

import by.hurynovich.mus_overview.converter.DTOEntityConverter;
import by.hurynovich.mus_overview.dto.impl.TagDTO;
import by.hurynovich.mus_overview.entity.impl.TagEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("tagConverter")
public class TagConverter implements DTOEntityConverter<TagDTO, TagEntity> {
    @Override
    public TagDTO convertToDTO(final TagEntity tagEntity) {
        if (tagEntity == null) {
            return null;
        } else {
            final TagDTO tagDto = new TagDTO();
            tagDto.setId(tagEntity.getId());
            tagDto.setName(tagEntity.getName());
            return tagDto;
        }
    }

    @Override
    public TagEntity convertToEntity(final TagDTO tagDto) {
        if (tagDto == null) {
            return null;
        } else {
            final TagEntity tagEntity = new TagEntity();
            tagEntity.setId(tagDto.getId());
            tagEntity.setName(tagDto.getName());
            return tagEntity;
        }
    }

    public List<TagDTO> convertToDTO(final List<TagEntity> tagEntities) {
        if (tagEntities == null) {
            return null;
        } else {
            final List<TagDTO> tagDtos = new ArrayList<>();
            tagEntities.forEach(tagEntity -> tagDtos.add(convertToDTO(tagEntity)));
            return tagDtos;
        }
    }

    public List<TagEntity> convertToEntity(final List<TagDTO> tagDtos) {
        if (tagDtos == null) {
            return null;
        } else {
            final List<TagEntity> tagEntities = new ArrayList<>();
            tagDtos.forEach(tagDto -> tagEntities.add(convertToEntity(tagDto)));
            return tagEntities;
        }
    }
}
