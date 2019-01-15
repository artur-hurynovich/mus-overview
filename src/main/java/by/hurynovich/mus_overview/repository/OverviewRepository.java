package by.hurynovich.mus_overview.repository;

import by.hurynovich.mus_overview.entity.impl.OverviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OverviewRepository extends JpaRepository<OverviewEntity, Long> {
    List<OverviewEntity> findAllBySubgroupId(long subgroupId);
    OverviewEntity findById(long id);
    long countBySubgroupId(final long subgroupId);
}
