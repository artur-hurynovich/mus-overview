package by.hurynovich.mus_overview.vaadin.from;

import by.hurynovich.mus_overview.dto.OverviewDTO;
import by.hurynovich.mus_overview.service.OverviewService;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewDateField;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewSubgroupField;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewTagField;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class OverviewForm extends Panel {

    private final TextField titleField;

    private final TextArea textField;

    private final OverviewDateField dateField;

    private final OverviewSubgroupField subgroupField;

    private final OverviewTagField tagField;

    private final OverviewService overviewService;

    public OverviewForm(final OverviewService overviewService, final OverviewDTO overviewDTO,
                        final Runnable onSave, final Runnable onDiscard) {
        this.overviewService = overviewService;
        titleField = new TextField("Title:");
        textField = new TextArea("Text");
        dateField = new OverviewDateField("Date:");
        subgroupField = new OverviewSubgroupField("Subgroup:");
        tagField = new OverviewTagField("Tags:");
    }

}
