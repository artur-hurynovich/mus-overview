package by.hurynovich.mus_overview.controller;

import by.hurynovich.mus_overview.dto.OverviewDTO;
import by.hurynovich.mus_overview.exception.OverviewCreationException;
import by.hurynovich.mus_overview.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/rest/overview")
public class OverviewController {
    private final OverviewService overviewService;

    @Autowired
    public OverviewController(final OverviewService overviewService) {
        this.overviewService = overviewService;
    }

    @PostMapping("/create")
    public OverviewDTO createOverview(@RequestBody final OverviewDTO overviewDTO) throws OverviewCreationException {
        return overviewService.createOverview(overviewDTO);
    }

    @GetMapping("/byId")
    public OverviewDTO getOverviewById(final HttpServletRequest request) {
        final String overviewIdParam = request.getParameter("id");
        final long overviewId = Long.valueOf(overviewIdParam);
        return overviewService.getOverviewById(overviewId);
    }

    @GetMapping("/all")
    public List<OverviewDTO> getAllOverviews() {
        return overviewService.getAllOverviews();
    }

    @GetMapping("/allBySubgroupId")
    public List<OverviewDTO> getAllOverviewsBySubgroupId(final HttpServletRequest request) {
        final String subgroupIdParam = request.getParameter("subgroupId");
        final long subgroupId = Long.valueOf(subgroupIdParam);
        return overviewService.getAllOverviewsBySubgroupId(subgroupId);
    }

    @GetMapping("/allByTags")
    public List<OverviewDTO> getAllOverviewsByTags(final HttpServletRequest request) {
        final String[] tagNames = request.getParameterValues("tag");
        return overviewService.getAllOverviewsByTags(tagNames);
    }
}
