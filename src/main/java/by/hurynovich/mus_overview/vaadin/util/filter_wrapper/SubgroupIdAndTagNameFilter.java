package by.hurynovich.mus_overview.vaadin.util.filter_wrapper;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component("filter")
public class SubgroupIdAndTagNameFilter {

    private long subgroupId;

    private String tagName;

}
