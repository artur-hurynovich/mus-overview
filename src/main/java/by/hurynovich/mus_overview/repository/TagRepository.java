package by.hurynovich.mus_overview.repository;

import by.hurynovich.mus_overview.entity.impl.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
    TagEntity findByName(String tagName);
    List<TagEntity> findByNameContaining(final String tagName);
}
