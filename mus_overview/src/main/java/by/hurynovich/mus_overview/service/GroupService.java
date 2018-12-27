package by.hurynovich.mus_overview.service;

import by.hurynovich.mus_overview.converter.impl.GroupConverter;
import by.hurynovich.mus_overview.converter.impl.SubgroupConverter;
import by.hurynovich.mus_overview.entity.GroupEntity;
import by.hurynovich.mus_overview.entity.SubgroupEntity;
import by.hurynovich.mus_overview.dto.GroupDTO;
import by.hurynovich.mus_overview.dto.SubgroupDTO;
import by.hurynovich.mus_overview.exception.GroupCreationException;
import by.hurynovich.mus_overview.exception.SubgroupCreationException;
import by.hurynovich.mus_overview.repository.GroupRepository;
import by.hurynovich.mus_overview.repository.SubgroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        } catch (RuntimeException e) {
            final String exceptionMessage = "Group creation failed: " + e.getMessage();
            throw new GroupCreationException(exceptionMessage);
        }
    }

    public SubgroupDTO createSubgroup(final SubgroupDTO subgroupDto) throws SubgroupCreationException {
        final SubgroupEntity subgroupEntity = subgroupConverter.convertToEntity(subgroupDto);
        try {
            final SubgroupEntity savedSubgroupEntity = subgroupRepository.save(subgroupEntity);
            return subgroupConverter.convertToDTO(savedSubgroupEntity);
        } catch (RuntimeException e) {
            final String exceptionMessage = "Subgroup creation failed: " + e.getMessage();
            throw new SubgroupCreationException(exceptionMessage);
        }
    }

    public List<GroupDTO> getAllGroups() {
        return groupRepository.findAll().stream().map(groupConverter::convertToDTO).
                collect(Collectors.toList());
    }

    public List<SubgroupDTO> getAllSubgroupsByGroupId(long groupId) {
        return subgroupRepository.findAllByGroupId(groupId).stream().map(subgroupConverter::convertToDTO).
                collect(Collectors.toList());
    }
}
