package by.hurynovich.mus_overview.vaadin.form;

import by.hurynovich.mus_overview.dto.OverviewDTO;
import by.hurynovich.mus_overview.exception.OverviewCreationException;
import by.hurynovich.mus_overview.exception.OverviewUpdatingException;
import by.hurynovich.mus_overview.exception.SubgroupCreationException;
import by.hurynovich.mus_overview.exception.SubgroupUpdatingException;
import by.hurynovich.mus_overview.service.GroupService;
import by.hurynovich.mus_overview.service.OverviewService;
import by.hurynovich.mus_overview.service.TagService;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewDateField;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewSubgroupField;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewTagField;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.ValidationResult;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
import java.util.stream.Collectors;

public class OverviewForm extends Panel {

    private final VerticalLayout parentLayout;

    private final TextField titleField;

    private final TextArea textField;

    private final OverviewDateField dateField;

    private final OverviewSubgroupField subgroupField;

    private final OverviewTagField tagField;

    private final Binder<OverviewDTO> binder;

    private final HorizontalLayout buttonsLayout;

    private final Button saveButton;

    private final Button cancelButton;

    private final OverviewService overviewService;

    @Autowired
    public OverviewForm(final OverviewService overviewService, final GroupService groupService,
                        final TagService tagService, final OverviewDTO overviewDTO,
                        final Runnable onSave, final Runnable onDiscard) {
        this.overviewService = overviewService;
        binder = new Binder<>(OverviewDTO.class);
        parentLayout = new VerticalLayout();
        titleField = new TextField("Title:");
        textField = new TextArea("Text");
        dateField = new OverviewDateField("Date:");
        subgroupField = new OverviewSubgroupField(groupService);
        tagField = new OverviewTagField(tagService);
        tagField.setCaption("Tags:");
        buttonsLayout = new HorizontalLayout();
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        binder.forField(titleField).withValidator(title -> title != null && !title.isEmpty(),
                "Please enter the title!").bind(OverviewDTO::getTitle, OverviewDTO::setTitle);
        binder.forField(textField).withValidator(text -> text != null && !text.isEmpty(),
                "Please enter the text!").bind(OverviewDTO::getText, OverviewDTO::setText);
        binder.forField(dateField).withValidator(Objects::nonNull, "Please enter the date!").
                withValidator(localDate -> localDate.isAfter(LocalDate.now(ZoneId.systemDefault()).minusDays(1)),
                        "Please choose the date not earlier than today!").
                bind(OverviewDTO::getDate, OverviewDTO::setDate);
        binder.forField(subgroupField).withValidator(subgroupId -> subgroupId != null && subgroupId != 0,
                "Please choose the corresponding subgroup!").bind(OverviewDTO::getSubgroupId, OverviewDTO::setSubgroupId);
        binder.forField(tagField).withValidator(tagDTOList -> tagDTOList != null && tagDTOList.size() > 0,
                "Please enter at least one tag!").bind(OverviewDTO::getTags, OverviewDTO::setTags);
        binder.readBean(overviewDTO);
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
                if (binder.writeBeanIfValid(overviewDTO)) {
                    if (overviewDTO.getId() == 0) {
                        overviewService.createOverview(overviewDTO);
                    } else {
                        overviewService.updateOverview(overviewDTO);
                    }
                    onSave.run();
                } else {
                    String validationError = binder.validate().getValidationErrors().stream().
                            map(ValidationResult::getErrorMessage).collect(Collectors.joining("\n"));
                    Notification.show("Warning!\n" + validationError,
                            Notification.Type.WARNING_MESSAGE);
                }
            } catch (OverviewCreationException | OverviewUpdatingException e) {
                Notification.show("Error!", "Overview saving failed!",
                        Notification.Type.ERROR_MESSAGE);
            }
        });
        saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        return saveButton;
    }

    private Button getCancelButton(final Runnable onDiscard) {
        cancelButton.addClickListener(clickEvent -> onDiscard.run());
        cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        return cancelButton;
    }

}
