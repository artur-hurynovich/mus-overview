package by.hurynovich.mus_overview.service.impl;

import by.hurynovich.mus_overview.converter.impl.OverviewConverter;
import by.hurynovich.mus_overview.converter.impl.TagConverter;
import by.hurynovich.mus_overview.dto.impl.TagDTO;
import by.hurynovich.mus_overview.entity.impl.OverviewEntity;
import by.hurynovich.mus_overview.dto.impl.OverviewDTO;
import by.hurynovich.mus_overview.entity.impl.TagEntity;
import by.hurynovich.mus_overview.repository.OverviewRepository;
import by.hurynovich.mus_overview.repository.TagRepository;
import by.hurynovich.mus_overview.service.IOverviewDTOService;
import by.hurynovich.mus_overview.util.TagNameFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("overviewService")
public class OverviewService implements IOverviewDTOService {

    @Autowired
    @Qualifier("tagNameFormatter")
    private TagNameFormatter tagNameFormatter;

    private final OverviewRepository overviewRepository;

    private final OverviewConverter overviewConverter;

    private final TagRepository tagRepository;

    private final TagConverter tagConverter;

    @Autowired
    public OverviewService(final OverviewRepository overviewRepository, final OverviewConverter overviewConverter,
                           final TagRepository tagRepository, final TagConverter tagConverter) {
        this.overviewRepository = overviewRepository;
        this.overviewConverter = overviewConverter;
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    @Transactional
    public OverviewDTO save(final OverviewDTO overviewDTO) {
        overviewDTO.getTags().forEach(tagNameFormatter::format);
        final List<TagEntity> toSaveTagsEntities = new ArrayList<>();
        for (final TagDTO tagDto : overviewDTO.getTags()) {
            final String tagName = tagDto.getName();
            final TagEntity existingTag = tagRepository.findByName(tagName);
            if (existingTag == null) {
                final TagEntity tagEntity = tagConverter.convertToEntity(tagDto);
                toSaveTagsEntities.add(tagEntity);
            } else {
                toSaveTagsEntities.add(existingTag);
            }
        }
        final OverviewEntity overviewEntity = overviewConverter.convertToEntity(overviewDTO);
        overviewEntity.setTags(toSaveTagsEntities);
        final OverviewEntity savedOverviewEntity = overviewRepository.save(overviewEntity);
        tagRepository.saveAll(toSaveTagsEntities);
        return overviewConverter.convertToDTO(savedOverviewEntity);
    }

    @Override
    public OverviewDTO findOne(final long id) {
        return overviewConverter.convertToDTO(overviewRepository.findById(id));
    }

    @Override
    public List<OverviewDTO> findAll() {
        return overviewRepository.findAll().stream().map(overviewConverter::convertToDTO).
                collect(Collectors.toList());
    }

    @Override
    public List<OverviewDTO> findAllBySubgroupId(final long subgroupId) {
        return overviewRepository.findAllBySubgroupId(subgroupId).stream().map(overviewConverter::convertToDTO).
                collect(Collectors.toList());
    }

    @Override
    public List<OverviewDTO> findAllByTagName(final String tagName) {
        return overviewRepository.findAllByTagsNameContaining(tagName).stream().map(overviewConverter::convertToDTO).
                collect(Collectors.toList());
    }

    @Override
    public List<OverviewDTO> findAllBySubgroupIdAndTagName(final long subgroupId, final String tagName) {
        return overviewRepository.findAllBySubgroupIdAndTagsNameContaining(subgroupId, tagName).stream().
                map(overviewConverter::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OverviewDTO update(final OverviewDTO overviewDTO) {
        final OverviewEntity overviewEntity = overviewRepository.findById(overviewDTO.getId());
        BeanUtils.copyProperties(overviewDTO, overviewEntity, "id", "tags");
        final List<TagDTO> tags = overviewDTO.getTags();
        final List<TagEntity> toSaveTagsEntities = new ArrayList<>();
        if (tags != null) {
            tags.forEach(tagNameFormatter::format);
            for (final TagDTO tagDto : tags) {
                final String tagName = tagDto.getName();
                final TagEntity existingTag = tagRepository.findByName(tagName);
                if (existingTag == null) {
                    final TagEntity tagEntity = tagConverter.convertToEntity(tagDto);
                    toSaveTagsEntities.add(tagEntity);
                } else {
                    toSaveTagsEntities.add(existingTag);
                }
            }
            overviewEntity.setTags(toSaveTagsEntities);
        }
        tagRepository.saveAll(toSaveTagsEntities);
        final OverviewEntity savedOverviewEntity = overviewRepository.save(overviewEntity);
        return overviewConverter.convertToDTO(savedOverviewEntity);
    }

    @Override
    public void delete(final OverviewDTO overviewDTO) {
        overviewRepository.delete(overviewConverter.convertToEntity(overviewDTO));
    }

    @Override
    public long count() {
        return overviewRepository.count();
    }

    @Override
    public long countBySubgroupId(final long subgroupId) {
        return overviewRepository.countBySubgroupId(subgroupId);
    }

    @Override
    public long countByTagName(final String tagName) {
        return overviewRepository.countByTagsNameContaining(tagName);
    }

    @Override
    public long countBySubgroupIdAndTagName(final long subgroupId, final String tagName) {
        return overviewRepository.countBySubgroupIdAndTagsNameContaining(subgroupId, tagName);
    }

}