package by.hurynovich.mus_overview.service;

import by.hurynovich.mus_overview.dto.impl.TagDTO;

public interface TagDTOService extends DTOService<TagDTO> {
    TagDTO findByName(final String tagName);
}
