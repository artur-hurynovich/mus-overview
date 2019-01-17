package by.hurynovich.mus_overview.service;

import by.hurynovich.mus_overview.dto.impl.TagDTO;

public interface ITagDTOService extends IDTOService<TagDTO> {

    TagDTO findByName(final String tagName);

}
