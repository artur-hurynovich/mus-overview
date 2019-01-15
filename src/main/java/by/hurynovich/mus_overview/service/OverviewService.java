package by.hurynovich.mus_overview.service;

import by.hurynovich.mus_overview.converter.impl.OverviewConverter;
import by.hurynovich.mus_overview.converter.impl.TagConverter;
import by.hurynovich.mus_overview.dto.impl.TagDTO;
import by.hurynovich.mus_overview.entity.impl.OverviewEntity;
import by.hurynovich.mus_overview.dto.impl.OverviewDTO;
import by.hurynovich.mus_overview.entity.impl.TagEntity;
import by.hurynovich.mus_overview.exception.OverviewCreationException;
import by.hurynovich.mus_overview.exception.OverviewDeletingException;
import by.hurynovich.mus_overview.exception.OverviewUpdatingException;
import by.hurynovich.mus_overview.repository.OverviewRepository;
import by.hurynovich.mus_overview.repository.TagRepository;
import by.hurynovich.mus_overview.util.TagNameFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OverviewService {
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

    @Transactional
    public OverviewDTO createOverview(final OverviewDTO overviewDto) throws OverviewCreationException {
        overviewDto.getTags().forEach(TagNameFormatter::format);
        try {
            final List<TagEntity> toSaveTagsEntities = new ArrayList<>();
            for (final TagDTO tagDto : overviewDto.getTags()) {
                final String tagName = tagDto.getName();
                final TagEntity existingTag = tagRepository.findByName(tagName);
                if (existingTag == null) {
                    final TagEntity tagEntity = tagConverter.convertToEntity(tagDto);
                    toSaveTagsEntities.add(tagEntity);
                } else {
                    toSaveTagsEntities.add(existingTag);
                }
            }

            final OverviewEntity overviewEntity = overviewConverter.convertToEntity(overviewDto);
            overviewEntity.setTags(toSaveTagsEntities);
            final OverviewEntity savedOverviewEntity = overviewRepository.save(overviewEntity);
            tagRepository.saveAll(toSaveTagsEntities);
            return overviewConverter.convertToDTO(savedOverviewEntity);
        } catch (final Exception e) {
            final String exceptionMessage = "Overview creating failed: " + e.getMessage();
            throw new OverviewCreationException(exceptionMessage);
        }
    }

    public OverviewDTO getOverviewById(final long overviewId) {
        final OverviewEntity overviewEntity = overviewRepository.findById(overviewId);
        if (overviewEntity.getId() == 0) {
            return null;
        } else {
            return overviewConverter.convertToDTO(overviewEntity);
        }
    }

    public List<OverviewDTO> getAllOverviews() {
        return overviewRepository.findAll().stream().map(overviewConverter::convertToDTO).
                collect(Collectors.toList());
    }

    public List<OverviewDTO> getAllOverviewsBySubgroupId(final long subgroupId) {
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

    @Transactional
    public OverviewDTO updateOverview(final OverviewDTO overviewDTO) throws OverviewUpdatingException {
        final OverviewEntity overviewEntity = overviewRepository.findById(overviewDTO.getId());
        final String name = overviewDTO.getName();
        if (name != null) {
            overviewEntity.setName(name);
        }
        final String text = overviewDTO.getText();
        if (text != null) {
            overviewEntity.setText(text);
        }
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
        try {
            final OverviewEntity savedOverviewEntity = overviewRepository.save(overviewEntity);
            tagRepository.saveAll(toSaveTagsEntities);
            return overviewConverter.convertToDTO(savedOverviewEntity);
        } catch (final Exception e) {
            final String exceptionMessage = "Overview updating failed: " + e.getMessage();
            throw new OverviewUpdatingException(exceptionMessage);
        }
    }

    public void deleteOverview(final long id) throws OverviewDeletingException {
        try {
            overviewRepository.deleteById(id);
        } catch (final Exception e) {
            final String exceptionMessage = "Overview deleting failed: " + e.getMessage();
            throw new OverviewDeletingException(exceptionMessage);
        }
    }

    public long overviewsByTagCount(final String tagName) {
        return getAllOverviewsByTag(tagName).size();
    }

    public long overviewsBySubgroupIdAndTagCount(final long subgroupId, final String tagName) {
        return getAllOverviewsBySubgroupIdAndTag(subgroupId, tagName).size();
    }

}