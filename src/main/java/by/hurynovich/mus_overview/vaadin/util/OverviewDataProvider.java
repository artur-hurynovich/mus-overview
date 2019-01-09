package by.hurynovich.mus_overview.vaadin.util;

import by.hurynovich.mus_overview.dto.OverviewDTO;
import by.hurynovich.mus_overview.exception.OverviewCreationException;
import by.hurynovich.mus_overview.service.OverviewService;
import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class OverviewDataProvider extends AbstractBackEndDataProvider<OverviewDTO, Long> {

    private final OverviewService overviewService;

    @Autowired
    public OverviewDataProvider(final OverviewService overviewService) {
        this.overviewService = overviewService;
    }

    public void save(final OverviewDTO overviewDTO) throws OverviewCreationException {
        overviewService.createOverview(overviewDTO);
    }

    public OverviewDTO findOne(final long id) {
        return overviewService.getOverviewById(id);
    }

    public List<OverviewDTO> findAll() {
        return overviewService.getAllOverviews();
    }

    public List<OverviewDTO> findAllBySubgroupId(final long subgroupId) {
        return overviewService.getAllOverviewsBySubgroupId(subgroupId);
    }

    public List<OverviewDTO> findAllByTags(final String[] tags) {
        return overviewService.getAllOverviewsByTags(tags);
    }

    public void update(final OverviewDTO overviewDTO) throws OverviewUpdatingException {
        overviewService.updateOverview(overviewDTO);
    }

    public void delete(final OverviewDTO overviewDTO) throws OverviewDeletingException {
        overviewService.deleteOverview(overviewDTO.getId());
    }

    @Override
    protected Stream<OverviewDTO> fetchFromBackEnd(final Query<OverviewDTO, Long> query) {
        return findAll().stream();
    }

    @Override
    protected int sizeInBackEnd(final Query<OverviewDTO, Long> query) {
        return 0;
    }

}
