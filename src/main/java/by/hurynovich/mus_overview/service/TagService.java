package by.hurynovich.mus_overview.service;

import by.hurynovich.mus_overview.converter.impl.TagConverter;
import by.hurynovich.mus_overview.dto.impl.TagDTO;
import by.hurynovich.mus_overview.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    private final TagConverter tagConverter;

    @Autowired
    public TagService(final TagRepository tagRepository, final TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    public List<TagDTO> getAllTags() {
        return tagConverter.convertToDTO(tagRepository.findAll());
    }

    public TagDTO getTagByName(final String tagName) {
        return tagConverter.convertToDTO(tagRepository.findByName(tagName));
    }

}
