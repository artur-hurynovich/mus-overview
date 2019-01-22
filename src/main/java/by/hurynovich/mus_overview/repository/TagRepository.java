package by.hurynovich.mus_overview.repository;

import by.hurynovich.mus_overview.entity.impl.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("tagRepository")
public interface TagRepository extends JpaRepository<TagEntity, Long> {
    TagEntity findById(final long id);
    TagEntity findByName(String tagName);
}
