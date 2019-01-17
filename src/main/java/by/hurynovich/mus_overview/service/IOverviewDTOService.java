package by.hurynovich.mus_overview.service;

import by.hurynovich.mus_overview.dto.impl.OverviewDTO;

import java.util.List;

public interface IOverviewDTOService extends IDTOService<OverviewDTO> {

    List<OverviewDTO> findAllBySubgroupId(final long subgroupId);

    List<OverviewDTO> findAllByTag(final String tagName);

    List<OverviewDTO> findAllBySubgroupIdAndTag(final long subgroupId, final String tagName);

    long countBySubgroupId(final long subgroupId);

    long countByTag(final String tagName);

    long countBySubgroupIdAndTag(final long subgroupId, final String tagName);

}
