package by.hurynovich.mus_overview.util;

import by.hurynovich.mus_overview.dto.TagDTO;
import by.hurynovich.mus_overview.entity.TagEntity;
import org.springframework.stereotype.Service;

@Service
public class TagNameFormatter {
    private final static String replaceRegex = "[^a-z0-9]";

    public static void format(final TagDTO tagDTO) {
        final String tagName = tagDTO.getName();
        final String formattedTagName = format(tagName);
        tagDTO.setName(formattedTagName);
    }

    public static String format(final String tagName) {
        return tagName.toLowerCase().replaceAll(replaceRegex, "");
    }
}
