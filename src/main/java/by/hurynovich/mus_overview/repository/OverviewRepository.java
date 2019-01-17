package by.hurynovich.mus_overview.repository;

import by.hurynovich.mus_overview.entity.impl.OverviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OverviewRepository extends JpaRepository<OverviewEntity, Long> {
    List<OverviewEntity> findAllBySubgroupId(final long subgroupId);
    List<OverviewEntity> findAllByTagsNameContaining(final String tagName);
    List<OverviewEntity> findAllBySubgroupIdAndTagsNameContaining(final long subgroupId, final String tagName);
    OverviewEntity findById(final long id);
    long countBySubgroupId(final long subgroupId);
    long countByTagsNameContaining(final String tagName);
    long countBySubgroupIdAndTagsNameContaining(final long subgroupId, final String tagName);
}
