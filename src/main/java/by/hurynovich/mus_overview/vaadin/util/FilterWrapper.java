package by.hurynovich.mus_overview.vaadin.util;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component("filter")
public class FilterWrapper {

    private long subgroupId;

    private String tagName;

}
