package by.hurynovich.mus_overview.vaadin.form.impl;

import by.hurynovich.mus_overview.dto.impl.OverviewDTO;
import by.hurynovich.mus_overview.service.OverviewDTOService;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewDateField;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewSubgroupField;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewTagField;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import com.vaadin.data.ValidationResult;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Collectors;

@Component("overviewForm")
public class OverviewForm extends AbstractDTOForm<OverviewDTO> {
    private final OverviewDTOService overviewService;
    private final OverviewDateField dateField;
    private final OverviewSubgroupField subgroupField;
    private final OverviewTagField tagField;
    private OverviewDTO overviewDTO;
    private TextField nameField;
    private TextArea textField;
    private Runnable onSave;
    private Runnable onDiscard;

    @Autowired
    public OverviewForm(final @Qualifier("overviewService") OverviewDTOService overviewService,
                        final @Qualifier("overviewDateField") OverviewDateField dateField,
                        final @Qualifier("overviewSubgroupField") OverviewSubgroupField subgroupField,
                        final @Qualifier("overviewTagField") OverviewTagField tagField) {
        this.overviewService = overviewService;
        this.dateField = dateField;
        this.subgroupField = subgroupField;
        this.tagField = tagField;
    }

    @PostConstruct
    public void init() {
        setupButtonsLayout();
        setupBinder();
        setupParentLayout();
        setContent(getParentLayout());
    }

    @Override
    public void setupForm(final OverviewDTO overviewDTO, final Runnable onSave, final Runnable onDiscard) {
        this.overviewDTO = overviewDTO;
        getBinder().readBean(overviewDTO);
        this.onSave = onSave;
        this.onDiscard = onDiscard;
    }

    private void setupButtonsLayout() {
        setupSaveButton();
        setupCancelButton();
        getButtonsLayout().addComponents(getSaveButton(), getCancelButton());
    }

    private void setupSaveButton() {
        getSaveButton().addClickListener(clickEvent -> {
            if (getBinder().writeBeanIfValid(overviewDTO)) {
                if (overviewDTO.getId() == 0) {
                    overviewService.save(overviewDTO);
                    Notification.show("Overview \'" + overviewDTO.getName() + "\' created!",
                            Notification.Type.HUMANIZED_MESSAGE);
                } else {
                    overviewService.update(overviewDTO);
                    Notification.show("Overview updated!",
                            Notification.Type.HUMANIZED_MESSAGE);
                }
                onSave.run();
            } else {
                final String validationError = getBinder().validate().getValidationErrors().stream().
                        map(ValidationResult::getErrorMessage).collect(Collectors.joining(";\n"));
                Notification.show("Warning!\n" + validationError,
                        Notification.Type.WARNING_MESSAGE);
            }
        });
    }

    private void setupCancelButton() {
        getCancelButton().addClickListener(clickEvent -> onDiscard.run());
    }

    private void setupBinder() {
        getBinder().forField(getNameField()).withValidator(name -> name != null && !name.isEmpty(),
                "Please, enter the overview name!").bind(OverviewDTO::getName, OverviewDTO::setName);
        getBinder().forField(getTextField()).withValidator(text -> text != null && !text.isEmpty(),
                "Please, enter the overview text!").bind(OverviewDTO::getText, OverviewDTO::setText);
        getBinder().forField(getDateField()).withValidator(date -> date != null &&
                        date.isBefore(LocalDate.now(ZoneId.systemDefault()).plusDays(1)),
                "Please, enter the overview date (it shouldn't be later than today)!").
                bind(OverviewDTO::getDate, OverviewDTO::setDate);
        getBinder().forField(getSubgroupField()).withValidator(subgroupId -> subgroupId != null && subgroupId != 0,
                "Please, select the corresponding subgroup!").
                bind(OverviewDTO::getSubgroupId, OverviewDTO::setSubgroupId);
        getBinder().forField(getTagField()).withValidator(tags -> tags != null && tags.size() > 0,
                "Please, enter at least one tag!").bind(OverviewDTO::getTags, OverviewDTO::setTags);
    }

    private void setupParentLayout() {
        getParentLayout().addComponents(getNameField(), getTextField(),
                getDateField(), getSubgroupField(), getTagField(), getButtonsLayout());
    }

    private TextField getNameField() {
        if (nameField == null) {
            nameField = new TextField("Name:");
        }
        return nameField;
    }

    private TextArea getTextField() {
        if (textField == null) {
            textField = new TextArea("Text:");
        }
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
}
