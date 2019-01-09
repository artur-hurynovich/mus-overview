package by.hurynovich.mus_overview.repository;

import by.hurynovich.mus_overview.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    GroupEntity findById(final long id);
}
