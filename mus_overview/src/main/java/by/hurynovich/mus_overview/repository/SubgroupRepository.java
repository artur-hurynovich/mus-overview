package by.hurynovich.mus_overview.repository;

import by.hurynovich.mus_overview.entity.SubgroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubgroupRepository extends JpaRepository<SubgroupEntity, Long> {
    List<SubgroupEntity> findAllByGroupId(long groupId);
    SubgroupEntity findById(long id);
}
