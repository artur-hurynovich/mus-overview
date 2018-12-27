package by.hurynovich.mus_overview.repository;

import by.hurynovich.mus_overview.entity.OverviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OverviewRepository extends JpaRepository<OverviewEntity, Long> {
    OverviewEntity findById(long overviewId);
    List<OverviewEntity> findAllBySubgroupId(long subgroupId);
}
