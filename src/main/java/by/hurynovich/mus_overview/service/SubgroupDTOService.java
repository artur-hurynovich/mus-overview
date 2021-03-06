package by.hurynovich.mus_overview.service;

import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;

import java.util.List;

public interface SubgroupDTOService extends DTOService<SubgroupDTO> {
    List<SubgroupDTO> findAllByGroupId(final long groupId);

    long countByGroupId(final long groupId);
}
