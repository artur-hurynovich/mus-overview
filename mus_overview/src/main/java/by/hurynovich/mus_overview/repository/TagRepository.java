package by.hurynovich.mus_overview.repository;

import by.hurynovich.mus_overview.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
    TagEntity findByName(String name);
    TagEntity findById(long id);
}
