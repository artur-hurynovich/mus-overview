package by.hurynovich.mus_overview.vaadin.form.impl;

import by.hurynovich.mus_overview.dto.impl.OverviewDTO;
import by.hurynovich.mus_overview.service.IGroupDTOService;
import by.hurynovich.mus_overview.service.IOverviewDTOService;
import by.hurynovich.mus_overview.service.ISubgroupDTOService;
import by.hurynovich.mus_overview.service.ITagDTOService;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Component("overviewForm")
public class OverviewForm extends AbstractDTOForm<OverviewDTO> {

    @Autowired
    @Qualifier("groupService")
    private IGroupDTOService groupService;

    @Autowired
    @Qualifier("subgroupService")
    private ISubgroupDTOService subgroupService;

    @Autowired
    @Qualifier("overviewService")
    private IOverviewDTOService overviewService;

    @Autowired
    @Qualifier("tagService")
    private ITagDTOService tagService;

    private OverviewDTO overviewDTO;

    private TextField nameField;

    private TextArea textField;

    @Autowired
    @Qualifier("overviewDateField")
    private OverviewDateField dateField;

    @Autowired
    @Qualifier("overviewSubgroupField")
    private OverviewSubgroupField subgroupField;

    @Autowired
    @Qualifier("overviewTagField")
    private OverviewTagField tagField;

    private Runnable onSave;

    private Runnable onDiscard;

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
        getBinder().forField(getSubgroupField()).withValidator(Objects::nonNull,
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
            textField = new TextArea("Text");
        }
        return textField;
    }

    private OverviewDateField getDateField() {
        if (dateField == null) {
            dateField = new OverviewDateField();
            dateField.setCaption("Date:");
        }
        return dateField;
    }

    private OverviewSubgroupField getSubgroupField() {
        if (subgroupField == null) {
            subgroupField = new OverviewSubgroupField();
        }
        return subgroupField;
    }

    private OverviewTagField getTagField() {
        if (tagField == null) {
            tagField = new OverviewTagField();
            tagField.setCaption("Tags:");
        }
        return tagField;
    }

}
