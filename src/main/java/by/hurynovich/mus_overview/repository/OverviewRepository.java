package by.hurynovich.mus_overview.repository;

import by.hurynovich.mus_overview.entity.impl.OverviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OverviewRepository extends JpaRepository<OverviewEntity, Long> {
    List<OverviewEntity> findAllByTags(final String tagName);
    List<OverviewEntity> findAllBySubgroupIdAndTags(final long subgroupId, final String tagName);
    OverviewEntity findById(final long id);
    long countByTags(final String tagName);
    long countBySubgroupIdAndTags(final long subgroupId, final String tagName);
}
