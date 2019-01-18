package by.hurynovich.mus_overview.util;

import by.hurynovich.mus_overview.dto.impl.TagDTO;
import org.springframework.stereotype.Service;

@Service("tagNameFormatter")
public class TagNameFormatter {
    private final static String replaceRegex = "[^a-z0-9]";

    public void format(final TagDTO tagDTO) {
        final String tagName = tagDTO.getName();
        final String formattedTagName = format(tagName);
        tagDTO.setName(formattedTagName);
    }

    public String format(final String tagName) {
        return tagName.toLowerCase().replaceAll(replaceRegex, "");
    }
}
