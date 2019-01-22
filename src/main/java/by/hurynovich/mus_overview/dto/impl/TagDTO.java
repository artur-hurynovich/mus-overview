package by.hurynovich.mus_overview.dto.impl;

import by.hurynovich.mus_overview.dto.AbstractNamedDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TagDTO extends AbstractNamedDTO {
    private List<OverviewDTO> overviews;
}
