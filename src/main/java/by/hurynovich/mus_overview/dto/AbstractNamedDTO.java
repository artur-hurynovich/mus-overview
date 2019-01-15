package by.hurynovich.mus_overview.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AbstractNamedDTO extends AbstractDTO {

    private String name;

}
