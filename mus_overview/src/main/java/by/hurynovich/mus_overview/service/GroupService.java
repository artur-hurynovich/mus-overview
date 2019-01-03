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

    public ResponseEntity<String> deleteGroup(long id){
        if(groupRepository.findById(id) != null){
            groupRepository.deleteById(id);
            return ResponseEntity.status(200).body("Group was deleted");
        }
        return ResponseEntity.status(400).body("Group doesn't exist");
    }

    public ResponseEntity<String> deleteSubgroup(long id){
        if(subgroupRepository.findById(id) != null){
            subgroupRepository.deleteById(id);
            return ResponseEntity.status(200).body("Subgroup was deleted");
        }
        return ResponseEntity.status(400).body("Subgroup doesn't exist");
    }

    public ResponseEntity<String> changeGroup(final long id, final String new_name){
        if(groupRepository.existsById(id)){
            GroupEntity groupEntity = groupRepository.findById(id);
            groupEntity.setName(new_name);
            groupRepository.save(groupEntity);
            return ResponseEntity.status(200).body("Group was changed");
        }
        return ResponseEntity.status(400).body("Group doesn't exist");
    }

    public ResponseEntity<String> changeSubgroup(final long id, final String new_name){
        if(subgroupRepository.existsById(id)){
            SubgroupEntity subgroupEntity = subgroupRepository.findById(id);
            subgroupEntity.setName(new_name);
            subgroupRepository.save(subgroupEntity);
            return ResponseEntity.status(200).body("Subgroup was changed");
        }
        return ResponseEntity.status(400).body("Subgroup doesn't exist");
    }
}
