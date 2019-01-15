package by.hurynovich.mus_overview.service.impl;

import by.hurynovich.mus_overview.converter.impl.OverviewConverter;
import by.hurynovich.mus_overview.converter.impl.TagConverter;
import by.hurynovich.mus_overview.dto.impl.TagDTO;
import by.hurynovich.mus_overview.entity.impl.OverviewEntity;
import by.hurynovich.mus_overview.dto.impl.OverviewDTO;
import by.hurynovich.mus_overview.entity.impl.TagEntity;
import by.hurynovich.mus_overview.repository.OverviewRepository;
import by.hurynovich.mus_overview.repository.TagRepository;
import by.hurynovich.mus_overview.service.DTOService;
import by.hurynovich.mus_overview.util.TagNameFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OverviewService implements DTOService<OverviewDTO> {

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
        overviewDTO.getTags().forEach(TagNameFormatter::format);
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

    public List<OverviewDTO> findAllByTag(final String tagName) {
        return overviewRepository.findAllByTags(tagName).stream().map(overviewConverter::convertToDTO).
                collect(Collectors.toList());
    }

    public List<OverviewDTO> findAllBySubgroupIdAndTag(final long subgroupId, final String tagName) {
        return overviewRepository.findAllBySubgroupIdAndTags(subgroupId, tagName).stream().
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
            tags.forEach(TagNameFormatter::format);
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

    public long countByTag(final String tagName) {
        return overviewRepository.countByTags(tagName);
    }

    public long countBySubgroupIdAndTag(final long subgroupId, final String tagName) {
        return overviewRepository.countBySubgroupIdAndTags(subgroupId, tagName);
    }

    /*public List<OverviewDTO> getAllOverviewsBySubgroupId(final long subgroupId) {
        return overviewRepository.findAllBySubgroupId(subgroupId).stream().
                map(overviewConverter::convertToDTO).collect(Collectors.toList());
    }

    public List<OverviewDTO> getAllOverviewsByTag(final String tagName) {
        if (tagName == null || tagName.isEmpty()) {
            return getAllOverviews();
        } else {
            final List<TagEntity> tagEntities = tagRepository.findByNameContaining(tagName);
            final Set<OverviewEntity> uniqueOverviewEntities = new HashSet<>();
            tagEntities.forEach(tagEntity -> uniqueOverviewEntities.addAll(tagEntity.getOverviews()));
            return uniqueOverviewEntities.stream().map(overviewConverter::convertToDTO).collect(Collectors.toList());
        }
    }

    public List<OverviewDTO> getAllOverviewsBySubgroupIdAndTag(final long subgroupId, final String tagName) {
        if (tagName == null) {
            return getAllOverviewsBySubgroupId(subgroupId);
        } else {
            return getAllOverviewsByTag(tagName).stream().
                    filter(overviewDTO -> overviewDTO.getSubgroupId() == subgroupId).collect(Collectors.toList());
        }
    }

    public long overviewsByTagCount(final String tagName) {
        return getAllOverviewsByTag(tagName).size();
    }

    public long overviewsBySubgroupIdAndTagCount(final long subgroupId, final String tagName) {
        return getAllOverviewsBySubgroupIdAndTag(subgroupId, tagName).size();
    }*/

}