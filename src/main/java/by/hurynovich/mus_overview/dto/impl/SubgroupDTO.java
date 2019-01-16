package by.hurynovich.mus_overview.dto.impl;

import by.hurynovich.mus_overview.dto.AbstractNamedDTO;
import by.hurynovich.mus_overview.vaadin.annotation.Bind;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SubgroupDTO extends AbstractNamedDTO {

    @Bind(fieldClass = "by.hurynovich.mus_overview.vaadin.custom_field.SubgroupGroupField")
    private long groupId;

}
