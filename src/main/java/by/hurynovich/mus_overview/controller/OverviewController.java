package by.hurynovich.mus_overview.controller;

import by.hurynovich.mus_overview.dto.OverviewDTO;
import by.hurynovich.mus_overview.exception.OverviewCreationException;
import by.hurynovich.mus_overview.exception.OverviewDeletingException;
import by.hurynovich.mus_overview.exception.OverviewUpdatingException;
import by.hurynovich.mus_overview.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public OverviewDTO getOverviewById(final @RequestParam long id) {
        return overviewService.getOverviewById(id);
    }

    @GetMapping("/all")
    public List<OverviewDTO> getAllOverviews() {
        return overviewService.getAllOverviews();
    }

    @GetMapping("/allBySubgroupId")
    public List<OverviewDTO> getAllOverviewsBySubgroupId(final @RequestParam long id) {
        return overviewService.getAllOverviewsBySubgroupId(id);
    }

    @GetMapping("/allByTags")
    public List<OverviewDTO> getAllOverviewsByTags(final @RequestParam String[] tag) {
        return overviewService.getAllOverviewsByTags(tag);
    }

    @PostMapping("/update")
    public OverviewDTO updateOverview(final @RequestBody OverviewDTO overviewDTO) throws OverviewUpdatingException {
        return overviewService.updateOverview(overviewDTO);
    }

    @PostMapping("/delete")
    public void deleteOverview(final @RequestParam long id) throws OverviewDeletingException {
        overviewService.deleteOverview(id);
    }
}
