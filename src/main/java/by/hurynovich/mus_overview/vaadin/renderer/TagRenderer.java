package by.hurynovich.mus_overview.vaadin.renderer;

import by.hurynovich.mus_overview.dto.impl.TagDTO;
import com.vaadin.ui.renderers.TextRenderer;
import elemental.json.Json;
import elemental.json.JsonValue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

public class TagRenderer extends TextRenderer {

    @SuppressWarnings("unchecked")
    @Override
    public JsonValue encode(final Object value) {
        if (value == null) {
            return super.encode(null);
        } else {
            return Json.create(encodeTags((List<TagDTO>) value));
        }
    }

    private String encodeTags(final List<TagDTO> tagDTOList) {
        return tagDTOList.stream().map(TagDTO::getName).collect(Collectors.joining(", "));
    }

}
