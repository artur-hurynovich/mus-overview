package by.hurynovich.mus_overview.vaadin.from;

import by.hurynovich.mus_overview.dto.OverviewDTO;
import by.hurynovich.mus_overview.exception.OverviewCreationException;
import by.hurynovich.mus_overview.exception.OverviewUpdatingException;
import by.hurynovich.mus_overview.service.GroupService;
import by.hurynovich.mus_overview.service.OverviewService;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewDateField;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewSubgroupField;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewTagField;
import com.vaadin.annotations.PropertyId;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class OverviewForm extends Panel {

    private final VerticalLayout parentLayout;

    @PropertyId("title")
    private final TextField titleField;

    @PropertyId("text")
    private final TextArea textField;

    @PropertyId("date")
    private final OverviewDateField dateField;

    @PropertyId("tags")
    private final OverviewSubgroupField subgroupField;

    private final Binder<OverviewDTO> binder;

    private final OverviewTagField tagField;

    private final HorizontalLayout buttonsLayout;

    private final Button saveButton;

    private final Button cancelButton;

    private final OverviewService overviewService;

    private final GroupService groupService;

    public OverviewForm(final OverviewService overviewService, final GroupService groupService,
                        final OverviewDTO overviewDTO, final Runnable onSave, final Runnable onDiscard) {
        this.overviewService = overviewService;
        this.groupService = groupService;
        binder = new Binder<>(OverviewDTO.class);
        binder.setBean(overviewDTO);
        binder.bindInstanceFields(this);
        parentLayout = new VerticalLayout();
        titleField = new TextField("Title:");
        textField = new TextArea("Text");
        dateField = new OverviewDateField("Date:");
        subgroupField = new OverviewSubgroupField(groupService);
        tagField = new OverviewTagField("Tags:");
        buttonsLayout = new HorizontalLayout();
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        setContent(getParentLayout(overviewDTO, onSave, onDiscard));
    }

    private VerticalLayout getParentLayout(final OverviewDTO overviewDTO,
                                           final Runnable onSave, final Runnable onDiscard) {
        parentLayout.addComponents(getTitleField(), getTextField(), getDateField(), getSubgroupField(),
                getTagField(), getButtonsLayout(overviewDTO, onSave, onDiscard));
        return parentLayout;
    }

    private TextField getTitleField() {
        return titleField;
    }

    private TextArea getTextField() {
        return textField;
    }

    private OverviewDateField getDateField() {
        return dateField;
    }

    private OverviewSubgroupField getSubgroupField() {
        return subgroupField;
    }

    private OverviewTagField getTagField() {
        return tagField;
    }

    private HorizontalLayout getButtonsLayout(final OverviewDTO overviewDTO,
                                              final Runnable onSave, final Runnable onDiscard) {
        buttonsLayout.addComponents(getSaveButton(overviewDTO, onSave),
                getCancelButton(onDiscard));
        return buttonsLayout;
    }

    private Button getSaveButton(final OverviewDTO overviewDTO, final Runnable onSave) {
        saveButton.addClickListener(clickEvent -> {
            try {
                if (overviewDTO.getId() == 0) {
                    binder.writeBean(overviewDTO);
                    final OverviewDTO overviewToSave = binder.getBean();
                    overviewService.createOverview(overviewToSave);
                } else {
                    binder.writeBean(overviewDTO);
                    final OverviewDTO overviewToSave = binder.getBean();
                    overviewService.updateOverview(overviewToSave);
                }
                onSave.run();
            } catch (ValidationException e) {
                Notification.show("Warning!", "Form is not valid!",
                        Notification.Type.WARNING_MESSAGE);
            } catch (OverviewCreationException | OverviewUpdatingException e) {
                Notification.show("Error!", "Overview saving failed!",
                        Notification.Type.ERROR_MESSAGE);
            }
        });
        return saveButton;
    }

    private Button getCancelButton(final Runnable onDiscard) {
        return cancelButton;
    }

}
