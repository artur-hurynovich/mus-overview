package by.hurynovich.mus_overview.service;

import by.hurynovich.mus_overview.converter.impl.TagConverter;
import by.hurynovich.mus_overview.dto.TagDTO;
import by.hurynovich.mus_overview.entity.TagEntity;
import by.hurynovich.mus_overview.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final TagConverter tagConverter;

    @Autowired
    public TagService(final TagRepository tagRepository, final TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    public ResponseEntity<String> createTag(final TagDTO tagDto){
        tagRepository.save(tagConverter.convertToEntity(tagDto));
        return ResponseEntity.status(200).body("Tag was created");
    }

    public ResponseEntity<String> deleteTag(long id){
        if(tagRepository.findById(id) != null){
            tagRepository.deleteById(id);
            return ResponseEntity.status(200).body("Tag was deleted");
        }
        return ResponseEntity.status(400).body("Tag doesn't exist");
    }

    public ResponseEntity<String> changeTag(final long id, final String new_name){
        if(tagRepository.existsById(id)){
            TagEntity tagEntity = tagRepository.findById(id);
            tagEntity.setName(new_name);
            tagRepository.save(tagEntity);
            return ResponseEntity.status(200).body("Tag was changed");
        }
        return ResponseEntity.status(400).body("Tag doesn't exist");
    }
}
