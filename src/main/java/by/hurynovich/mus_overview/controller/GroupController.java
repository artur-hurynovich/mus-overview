package by.hurynovich.mus_overview.controller;

import by.hurynovich.mus_overview.dto.GroupDTO;
import by.hurynovich.mus_overview.dto.SubgroupDTO;
import by.hurynovich.mus_overview.exception.GroupCreationException;
import by.hurynovich.mus_overview.exception.GroupDeletingException;
import by.hurynovich.mus_overview.exception.GroupUpdatingException;
import by.hurynovich.mus_overview.exception.SubgroupCreationException;
import by.hurynovich.mus_overview.exception.SubgroupDeletingException;
import by.hurynovich.mus_overview.exception.SubgroupUpdatingException;
import by.hurynovich.mus_overview.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/rest/group")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(final GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/createGroup")
    public GroupDTO createGroup(final @RequestBody GroupDTO groupDTO) throws GroupCreationException {
        return groupService.createGroup(groupDTO);
    }

    @PostMapping("/createSubgroup")
    public SubgroupDTO createSubgroup(final @RequestBody SubgroupDTO subgroupDTO) throws SubgroupCreationException {
        return groupService.createSubgroup(subgroupDTO);
    }

    @GetMapping("/allGroups")
    public List<GroupDTO> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/allSubgroups")
    public List<SubgroupDTO> getAllSubgroupsByGroupId(final HttpServletRequest request) {
        final String groupIdParam = request.getParameter("groupId");
        final long groupId = Long.valueOf(groupIdParam);
        return groupService.getAllSubgroupsByGroupId(groupId);
    }

    @PostMapping("/updateGroup")
    public GroupDTO updateGroup(final @RequestBody GroupDTO groupDTO) throws GroupUpdatingException {
        return groupService.updateGroup(groupDTO);
    }

    @PostMapping("/updateSubgroup")
    public SubgroupDTO updateSubgroup(final @RequestBody SubgroupDTO subgroupDTO) throws SubgroupUpdatingException {
        return groupService.updateSubgroup(subgroupDTO);
    }

    @PostMapping("/deleteGroup")
    public void deleteGroup(final @RequestParam long id) throws GroupDeletingException {
        groupService.deleteGroup(id);
    }

    @PostMapping("/deleteSubgroup")
    public void deleteSubgroup(final @RequestParam long id) throws SubgroupDeletingException {
        groupService.deleteSubgroup(id);
    }
}