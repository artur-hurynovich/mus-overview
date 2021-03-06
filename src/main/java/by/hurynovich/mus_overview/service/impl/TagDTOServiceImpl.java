package by.hurynovich.mus_overview.service.impl;

import by.hurynovich.mus_overview.converter.impl.TagConverter;
import by.hurynovich.mus_overview.dto.impl.TagDTO;
import by.hurynovich.mus_overview.entity.impl.TagEntity;
import by.hurynovich.mus_overview.repository.TagRepository;
import by.hurynovich.mus_overview.service.TagDTOService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("tagService")
public class TagDTOServiceImpl implements TagDTOService {
    private final TagRepository tagRepository;
    private final TagConverter tagConverter;

    @Autowired
    public TagDTOServiceImpl(final @Qualifier("tagRepository") TagRepository tagRepository,
                             final @Qualifier("tagConverter") TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    public TagDTO save(final TagDTO tagDTO) {
        final TagEntity tagEntity = tagConverter.convertToEntity(tagDTO);
        return tagConverter.convertToDTO(tagRepository.save(tagEntity));
    }

    @Override
    public TagDTO findOne(final long id) {
        return tagConverter.convertToDTO(tagRepository.findById(id));
    }

    @Override
    public TagDTO findByName(final String tagName) {
        return tagConverter.convertToDTO(tagRepository.findByName(tagName));
    }

    @Override
    public List<TagDTO> findAll() {
        return tagRepository.findAll().stream().map(tagConverter::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public TagDTO update(final TagDTO tagDTO) {
        final TagEntity tagEntity = tagRepository.findById(tagDTO.getId());
        BeanUtils.copyProperties(tagDTO, tagEntity, "overviews");
        return tagConverter.convertToDTO(tagRepository.save(tagEntity));
    }

    @Override
    public void delete(final TagDTO tagDTO) {
        tagRepository.delete(tagConverter.convertToEntity(tagDTO));
    }

    @Override
    public long count() {
        return tagRepository.count();
    }
}
