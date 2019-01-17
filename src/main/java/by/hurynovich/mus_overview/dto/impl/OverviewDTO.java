package by.hurynovich.mus_overview.dto.impl;

import by.hurynovich.mus_overview.dto.AbstractNamedDTO;
import by.hurynovich.mus_overview.vaadin.annotation.GridColumn;
import by.hurynovich.mus_overview.vaadin.annotation.GridRenderer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OverviewDTO extends AbstractNamedDTO {

    @GridColumn(caption = "Text", position = 20)
    private String text;

    @GridColumn(caption = "Date", position = 30)
    private LocalDate date;

    private long subgroupId;

    @GridColumn(caption = "Tags", position = 40)
    @GridRenderer(rendererClass = "by.hurynovich.mus_overview.vaadin.renderer.TagRenderer")
    private List<TagDTO> tags;

}
