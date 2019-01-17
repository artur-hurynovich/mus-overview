package by.hurynovich.mus_overview.service;

import by.hurynovich.mus_overview.dto.impl.OverviewDTO;

import java.util.List;

public interface IOverviewDTOService extends IDTOService<OverviewDTO> {

    List<OverviewDTO> findAllBySubgroupId(final long subgroupId);

    List<OverviewDTO> findAllByTagName(final String tagName);

    List<OverviewDTO> findAllBySubgroupIdAndTagName(final long subgroupId, final String tagName);

    long countBySubgroupId(final long subgroupId);

    long countByTagName(final String tagName);

    long countBySubgroupIdAndTagName(final long subgroupId, final String tagName);

}
