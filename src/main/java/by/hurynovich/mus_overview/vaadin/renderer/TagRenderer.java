package by.hurynovich.mus_overview.vaadin.renderer;

import by.hurynovich.mus_overview.dto.TagDTO;
import com.vaadin.ui.renderers.TextRenderer;
import elemental.json.Json;
import elemental.json.JsonValue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class TagRenderer extends TextRenderer {

    @SuppressWarnings("unchecked")
    @Override
    public JsonValue encode(final Object value) {
        /*if (value == null) {
            return super.encode(null);
        } else if (correctType(value)){
            return Json.create(encodeTags((List<TagDTO>) value));
        } else {
            return Json.create(value.toString());
        }*/
        return Json.create(encodeTags((List<TagDTO>) value));
    }

    /*private boolean correctType(final Object object) {
        if (object.getClass() != List.class) {
            return false;
        } else {
            final ParameterizedType parameterizedType = (ParameterizedType) object.getClass().getGenericSuperclass();
            final Type[] genericTypes = parameterizedType.getActualTypeArguments();
            if (genericTypes.length != 1) {
                return false;
            } else  {
                return genericTypes[0].getTypeName().equals("by.hurynovich.mus_overview.dto.TagDTO");
            }
        }
    }*/

    private String encodeTags(final List<TagDTO> tagDTOList) {
        return tagDTOList.stream().map(TagDTO::getName).collect(Collectors.joining(" "));
    }

}
