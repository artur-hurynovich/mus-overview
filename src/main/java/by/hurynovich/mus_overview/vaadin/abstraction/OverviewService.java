package by.hurynovich.mus_overview.vaadin.abstraction;

import by.hurynovich.mus_overview.converter.impl.OverviewConverter;
import by.hurynovich.mus_overview.converter.impl.TagConverter;
import by.hurynovich.mus_overview.dto.impl.OverviewDTO;
import by.hurynovich.mus_overview.dto.impl.TagDTO;
import by.hurynovich.mus_overview.entity.impl.OverviewEntity;
import by.hurynovich.mus_overview.entity.impl.TagEntity;
import by.hurynovich.mus_overview.exception.OverviewCreationException;
import by.hurynovich.mus_overview.repository.OverviewRepository;
import by.hurynovich.mus_overview.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("abstractionOverviewService")
public class OverviewService implements AbstractService<OverviewDTO> {

    private final OverviewRepository overviewRepository;

    private final TagRepository tagRepository;

    private final OverviewConverter overviewConverter;

    private final TagConverter tagConverter;

    @Autowired
    public OverviewService(final OverviewRepository overviewRepository,
                           final TagRepository tagRepository,
                           final OverviewConverter overviewConverter,
                           final TagConverter tagConverter) {
        this.overviewRepository = overviewRepository;
        this.tagRepository = tagRepository;
        this.overviewConverter = overviewConverter;
        this.tagConverter = tagConverter;
    }

    @Override
    @Transactional
    public OverviewDTO save(final OverviewDTO overviewDTO) throws OverviewCreationException {
        try {
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
        } catch (final Exception e) {
            final String exceptionMessage = "Overview creating failed: " + e.getMessage();
            throw new OverviewCreationException(exceptionMessage);
        }
    }

    @Override
    public List<OverviewDTO> findAll() {
        return overviewRepository.findAll().stream().map(overviewConverter::convertToDTO).
                collect(Collectors.toList());
    }

    @Override
    public void delete(final OverviewDTO overviewDTO) {
        overviewRepository.delete(overviewConverter.convertToEntity(overviewDTO));
    }

    @Override
    public long count() {
        return overviewRepository.count();
    }
}
