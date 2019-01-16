package by.hurynovich.mus_overview.dto.impl;

import by.hurynovich.mus_overview.dto.AbstractNamedDTO;
import by.hurynovich.mus_overview.vaadin.annotation.Bind;
import by.hurynovich.mus_overview.vaadin.annotation.GridColumn;
import by.hurynovich.mus_overview.vaadin.annotation.GridRenderer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OverviewDTO extends AbstractNamedDTO {

    @GridColumn(caption = "Text", position = 20)
    @Bind(fieldClass = "com.vaadin.ui.TextArea")
    private String text;

    @GridColumn(caption = "Date", position = 30)
    @Bind(fieldClass = "by.hurynovich.mus_overview.vaadin.custom_field.OverviewDateField")
    private LocalDate date;

    @Bind(fieldClass = "by.hurynovich.mus_overview.vaadin.custom_field.OverviewSubgroupField")
    private long subgroupId;

    @GridColumn(caption = "Tags", position = 40)
    @GridRenderer(rendererClass = "by.hurynovich.mus_overview.vaadin.renderer.TagRenderer")
    @Bind(fieldClass = "by.hurynovich.mus_overview.vaadin.custom_field.OverviewTagField")
    private List<TagDTO> tags;

}
