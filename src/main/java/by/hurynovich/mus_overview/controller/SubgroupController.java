package by.hurynovich.mus_overview.controller;

import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.service.ISubgroupDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/rest/subgroup")
public class SubgroupController {

    private final ISubgroupDTOService subgroupService;

    @Autowired
    public SubgroupController(final ISubgroupDTOService subgroupService) {
        this.subgroupService = subgroupService;
    }

    @PostMapping("/create")
    public SubgroupDTO create(final @RequestBody SubgroupDTO subgroupDTO) {
        return subgroupService.save(subgroupDTO);
    }

    @GetMapping("/all")
    public List<SubgroupDTO> getAllSubgroupsByGroupId(final HttpServletRequest request) {
        final String groupIdParam = request.getParameter("groupId");
        final long groupId = Long.valueOf(groupIdParam);
        return subgroupService.findAllByGroupId(groupId);
    }

    @PostMapping("/update")
    public SubgroupDTO updateSubgroup(final @RequestBody SubgroupDTO subgroupDTO) {
        return subgroupService.update(subgroupDTO);
    }

    @PostMapping("/delete")
    public void deleteSubgroup(final @RequestBody SubgroupDTO subgroupDTO) {
        subgroupService.delete(subgroupDTO);
    }

}
