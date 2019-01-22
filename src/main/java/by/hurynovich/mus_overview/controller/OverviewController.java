package by.hurynovich.mus_overview.controller;

import by.hurynovich.mus_overview.dto.impl.OverviewDTO;
import by.hurynovich.mus_overview.service.OverviewDTOService;
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
    private final OverviewDTOService overviewService;

    @Autowired
    public OverviewController(final OverviewDTOService overviewService) {
        this.overviewService = overviewService;
    }

    @PostMapping("/create")
    public OverviewDTO createOverview(@RequestBody final OverviewDTO overviewDTO) {
        return overviewService.save(overviewDTO);
    }

    @GetMapping("/byId")
    public OverviewDTO getOverviewById(final @RequestParam long id) {
        return overviewService.findOne(id);
    }

    @GetMapping("/all")
    public List<OverviewDTO> getAllOverviews() {
        return overviewService.findAll();
    }

    @GetMapping("/allBySubgroupId")
    public List<OverviewDTO> getAllOverviewsBySubgroupId(final @RequestParam long id) {
        return overviewService.findAllBySubgroupId(id);
    }

    @GetMapping("/allByTags")
    public List<OverviewDTO> getAllOverviewsByTags(final @RequestParam String tagName) {
        return overviewService.findAllByTagName(tagName);
    }

    @PostMapping("/update")
    public OverviewDTO updateOverview(final @RequestBody OverviewDTO overviewDTO) {
        return overviewService.update(overviewDTO);
    }

    @PostMapping("/delete")
    public void deleteOverview(final @RequestBody OverviewDTO overviewDTO) {
        overviewService.delete(overviewDTO);
    }
}
