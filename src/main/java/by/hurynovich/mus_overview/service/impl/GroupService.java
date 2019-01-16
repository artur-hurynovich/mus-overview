package by.hurynovich.mus_overview.service.impl;

import by.hurynovich.mus_overview.converter.impl.GroupConverter;
import by.hurynovich.mus_overview.entity.impl.GroupEntity;
import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.repository.GroupRepository;
import by.hurynovich.mus_overview.service.IGroupDTOService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service("groupService")
public class GroupService implements IGroupDTOService {

    private final GroupRepository groupRepository;

    private final GroupConverter groupConverter;

    @Autowired
    public GroupService(final GroupRepository groupRepository, final GroupConverter groupConverter) {
        this.groupRepository = groupRepository;
        this.groupConverter = groupConverter;
    }

    @Override
    public GroupDTO save(final GroupDTO groupDTO) {
        final GroupEntity groupEntity = groupConverter.convertToEntity(groupDTO);
        return groupConverter.convertToDTO(groupRepository.save(groupEntity));
    }

    @Override
    public GroupDTO findOne(final long id) {
        return groupConverter.convertToDTO(groupRepository.getOne(id));
    }

    @Override
    public List<GroupDTO> findAll() {
        return groupRepository.findAll().stream().map(groupConverter::convertToDTO).
                collect(Collectors.toList());
    }

    @Override
    @Transactional
    public GroupDTO update(final GroupDTO groupDTO) {
        final GroupEntity groupEntity = groupRepository.getOne(groupDTO.getId());
        BeanUtils.copyProperties(groupDTO, groupEntity, "id");
        return groupConverter.convertToDTO(groupRepository.save(groupEntity));
    }

    @Override
    public void delete(final GroupDTO groupDTO) {
        groupRepository.delete(groupConverter.convertToEntity(groupDTO));
    }

    @Override
    public long count() {
        return groupRepository.count();
    }

}