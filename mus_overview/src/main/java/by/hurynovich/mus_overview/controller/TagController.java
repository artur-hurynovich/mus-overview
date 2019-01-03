package by.hurynovich.mus_overview.controller;

import by.hurynovich.mus_overview.service.TagService;
import by.hurynovich.mus_overview.dto.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/rest/tag")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(final TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTag(@RequestBody final TagDTO tagDto) {
        return tagService.createTag(tagDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable("id") final long id){
        return tagService.deleteTag(id);
    }

    @PostMapping("/change/{id}/{new_name}")
    public ResponseEntity<String> changeTag(@PathVariable("id") final long id, @PathVariable("new_name") final String new_name){
        return tagService.changeTag(id, new_name);
    }
}