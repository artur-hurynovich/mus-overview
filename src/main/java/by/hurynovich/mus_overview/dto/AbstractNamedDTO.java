package by.hurynovich.mus_overview.dto;

import by.hurynovich.mus_overview.vaadin.annotation.GridColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AbstractNamedDTO extends AbstractDTO {

    @GridColumn(caption = "Name", position = 10)
    private String name;

}
