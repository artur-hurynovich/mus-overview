package by.hurynovich.mus_overview.dto;

import by.hurynovich.mus_overview.vaadin.annotation.GridColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class AbstractNamedDTO extends AbstractDTO {

    @GridColumn(caption = "Name", position = 10)
    private String name;

}
