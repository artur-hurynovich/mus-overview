package by.hurynovich.mus_overview.service;

import by.hurynovich.mus_overview.converter.impl.GroupConverter;
import by.hurynovich.mus_overview.converter.impl.SubgroupConverter;
import by.hurynovich.mus_overview.entity.impl.GroupEntity;
import by.hurynovich.mus_overview.entity.impl.SubgroupEntity;
import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.exception.GroupCreationException;
import by.hurynovich.mus_overview.exception.GroupDeletingException;
import by.hurynovich.mus_overview.exception.GroupUpdatingException;
import by.hurynovich.mus_overview.exception.SubgroupCreationException;
import by.hurynovich.mus_overview.exception.SubgroupDeletingException;
import by.hurynovich.mus_overview.exception.SubgroupUpdatingException;
import by.hurynovich.mus_overview.repository.GroupRepository;
import by.hurynovich.mus_overview.repository.SubgroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupConverter groupConverter;
    private final SubgroupRepository subgroupRepository;
    private final SubgroupConverter subgroupConverter;

    @Autowired
    public GroupService(final GroupRepository groupRepository, final GroupConverter groupConverter,
                        final SubgroupRepository subgroupRepository, final SubgroupConverter subgroupConverter) {
        this.groupRepository = groupRepository;
        this.subgroupRepository = subgroupRepository;
        this.groupConverter = groupConverter;
        this.subgroupConverter = subgroupConverter;
    }

    public GroupDTO createGroup(final GroupDTO groupDto) throws GroupCreationException {
        final GroupEntity groupEntity = groupConverter.convertToEntity(groupDto);
        try {
            final GroupEntity savedGroupEntity = groupRepository.save(groupEntity);
            return groupConverter.convertToDTO(savedGroupEntity);
        } catch (final Exception e) {
            final String exceptionMessage = "Group creation failed: " + e.getMessage();
            throw new GroupCreationException(exceptionMessage);
        }
    }

    public SubgroupDTO createSubgroup(final SubgroupDTO subgroupDto) throws SubgroupCreationException {
        final SubgroupEntity subgroupEntity = subgroupConverter.convertToEntity(subgroupDto);
        try {
            final SubgroupEntity savedSubgroupEntity = subgroupRepository.save(subgroupEntity);
            return subgroupConverter.convertToDTO(savedSubgroupEntity);
        } catch (final Exception e) {
            final String exceptionMessage = "Subgroup creation failed: " + e.getMessage();
            throw new SubgroupCreationException(exceptionMessage);
        }
    }

    public GroupDTO getGroupById(final long id) {
        return groupConverter.convertToDTO(groupRepository.findById(id));
    }

    public SubgroupDTO getSubgroupById(final long id) {
        return subgroupConverter.convertToDTO(subgroupRepository.findById(id));
    }

    public List<GroupDTO> getAllGroups() {
        return groupRepository.findAll().stream().map(groupConverter::convertToDTO).
                collect(Collectors.toList());
    }

    public List<SubgroupDTO> getAllSubgroupsByGroupId(final long groupId) {
        return subgroupRepository.findAllByGroupId(groupId).stream().map(subgroupConverter::convertToDTO).
                collect(Collectors.toList());
    }

    @Transactional
    public GroupDTO updateGroup(final GroupDTO groupDTO) throws GroupUpdatingException {
        final GroupEntity groupEntity = groupRepository.getOne(groupDTO.getId());
        groupEntity.setName(groupDTO.getName());
        try {
            return groupConverter.convertToDTO(groupRepository.save(groupEntity));
        } catch (final Exception e) {
            final String exceptionMessage = "Group updating failed: " + e.getMessage();
            throw new GroupUpdatingException(exceptionMessage);
        }
    }

    @Transactional
    public SubgroupDTO updateSubgroup(final SubgroupDTO subgroupDTO) throws SubgroupUpdatingException {
        final SubgroupEntity subgroupEntity = subgroupRepository.getOne(subgroupDTO.getId());
        final String subgroupName = subgroupDTO.getName();
        if (subgroupName != null) {
            subgroupEntity.setName(subgroupName);
        }
        final long groupId = subgroupDTO.getGroupId();
        if (groupId != 0) {
            subgroupEntity.setGroupId(groupId);
        }
        try {
            return subgroupConverter.convertToDTO(subgroupRepository.save(subgroupEntity));
        } catch (final Exception e) {
            final String exceptionMessage = "Subgroup updating failed: " + e.getMessage();
            throw new SubgroupUpdatingException(exceptionMessage);
        }
    }

    public void deleteGroup(final long id) throws GroupDeletingException {
        try {
            groupRepository.deleteById(id);
        } catch (final Exception e) {
            final String exceptionMessage = "Group deleting failed: " + e.getMessage();
            throw new GroupDeletingException(exceptionMessage);
        }
    }

    public void deleteSubgroup(final long id) throws SubgroupDeletingException {
        try {
            subgroupRepository.deleteById(id);
        } catch (final Exception e) {
            final String exceptionMessage = "Subgroup deleting failed: " + e.getMessage();
            throw new SubgroupDeletingException(exceptionMessage);
        }
    }

    public long getGroupsCount() {
        return groupRepository.count();
    }

    public long getSubgroupsByGroupIdCount(final long groupId) {
        return subgroupRepository.countByGroupId(groupId);
    }

}