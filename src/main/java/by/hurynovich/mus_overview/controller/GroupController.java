package by.hurynovich.mus_overview.controller;

import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.service.IGroupDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/group")
public class GroupController {

    private final IGroupDTOService groupService;

    @Autowired
    public GroupController(final IGroupDTOService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/create")
    public GroupDTO create(final @RequestBody GroupDTO groupDTO) {
        return groupService.save(groupDTO);
    }

    @GetMapping("/all")
    public List<GroupDTO> getAll() {
        return groupService.findAll();
    }

    @PostMapping("/update")
    public GroupDTO update(final @RequestBody GroupDTO groupDTO) {
        return groupService.update(groupDTO);
    }

    @PostMapping("/delete")
    public void deleteGroup(final @RequestBody GroupDTO groupDTO) {
        groupService.delete(groupDTO);
    }

}
