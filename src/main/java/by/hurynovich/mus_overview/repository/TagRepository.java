package by.hurynovich.mus_overview.repository;

import by.hurynovich.mus_overview.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
    TagEntity findByName(String name);
    List<TagEntity> findByNameContaining(final String name);
}
