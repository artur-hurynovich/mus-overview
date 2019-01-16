package by.hurynovich.mus_overview.service.impl;

import by.hurynovich.mus_overview.converter.impl.SubgroupConverter;
import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.entity.impl.SubgroupEntity;
import by.hurynovich.mus_overview.repository.SubgroupRepository;
import by.hurynovich.mus_overview.service.ISubgroupDTOService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service("subgroupService")
public class SubgroupService implements ISubgroupDTOService {

    private final SubgroupRepository subgroupRepository;

    private final SubgroupConverter subgroupConverter;

    @Autowired
    public SubgroupService(final SubgroupRepository subgroupRepository, final SubgroupConverter subgroupConverter) {
        this.subgroupRepository = subgroupRepository;
        this.subgroupConverter = subgroupConverter;
    }

    @Override
    public SubgroupDTO save(final SubgroupDTO subgroupDTO) {
        final SubgroupEntity subgroupEntity = subgroupConverter.convertToEntity(subgroupDTO);
        return subgroupConverter.convertToDTO(subgroupRepository.save(subgroupEntity));
    }

    @Override
    public SubgroupDTO findOne(final long id) {
        return subgroupConverter.convertToDTO(subgroupRepository.getOne(id));
    }

    @Override
    public List<SubgroupDTO> findAll() {
        return subgroupRepository.findAll().stream().map(subgroupConverter::convertToDTO).
                collect(Collectors.toList());
    }

    @Override
    public List<SubgroupDTO> findAllByGroupId(final long groupId) {
        if (groupId == -1L) {
            return findAll();
        } else {
            return subgroupRepository.findAllByGroupId(groupId).stream().map(subgroupConverter::convertToDTO).
                    collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public SubgroupDTO update(final SubgroupDTO subgroupDTO) {
        final SubgroupEntity subgroupEntity = subgroupRepository.getOne(subgroupDTO.getId());
        BeanUtils.copyProperties(subgroupDTO, subgroupEntity, "id");
        return subgroupConverter.convertToDTO(subgroupRepository.save(subgroupEntity));
    }

    @Override
    public void delete(final SubgroupDTO subgroupDTO) {
        subgroupRepository.delete(subgroupConverter.convertToEntity(subgroupDTO));
    }

    @Override
    public long count() {
        return subgroupRepository.count();
    }

    @Override
    public long countByGroupId(final long groupId) {
        if (groupId == -1L) {
            return count();
        } else {
            return subgroupRepository.countByGroupId(groupId);
        }
    }

}
