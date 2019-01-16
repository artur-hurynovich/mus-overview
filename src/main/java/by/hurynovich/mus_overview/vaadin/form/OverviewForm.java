package by.hurynovich.mus_overview.vaadin.form;

import by.hurynovich.mus_overview.dto.impl.OverviewDTO;
import by.hurynovich.mus_overview.service.impl.GroupService;
import by.hurynovich.mus_overview.service.impl.OverviewService;
import by.hurynovich.mus_overview.service.impl.SubgroupService;
import by.hurynovich.mus_overview.service.impl.TagService;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewDateField;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewSubgroupField;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewTagField;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

public class OverviewForm extends Panel {

    private VerticalLayout parentLayout;

    private TextField nameField;

    private TextArea textField;

    private OverviewDateField dateField;

    private OverviewSubgroupField subgroupField;

    private OverviewTagField tagField;

    private Binder<OverviewDTO> binder;

    private HorizontalLayout buttonsLayout;

    private Button saveButton;

    private Button cancelButton;

    private final GroupService groupService;

    private final SubgroupService subgroupService;

    private final OverviewService overviewService;

    private final TagService tagService;

    public OverviewForm(final GroupService groupService, final SubgroupService subgroupService,
                        final OverviewService overviewService, final TagService tagService,
                        final OverviewDTO overviewDTO, final Runnable onSave, final Runnable onDiscard) {
        this.groupService = groupService;
        this.subgroupService = subgroupService;
        this.overviewService = overviewService;
        this.tagService = tagService;
        binder = new Binder<>(OverviewDTO.class);
        binder.forField(getNameField()).withValidator(name -> name != null && !name.isEmpty(),
                "Please enter the title!").bind(OverviewDTO::getName, OverviewDTO::setName);
        binder.forField(getTextField()).withValidator(text -> text != null && !text.isEmpty(),
                "Please enter the text!").bind(OverviewDTO::getText, OverviewDTO::setText);
        binder.forField(getDateField()).withValidator(Objects::nonNull, "Please enter the date!").
                withValidator(localDate -> localDate.isAfter(LocalDate.now(ZoneId.systemDefault()).minusDays(1)),
                        "Please choose the date not earlier than today!").
                bind(OverviewDTO::getDate, OverviewDTO::setDate);
        binder.forField(getSubgroupField()).
                withValidator(subgroupId -> subgroupId != null && subgroupId != 0,
                        "Please choose the corresponding subgroup!").
                bind(OverviewDTO::getSubgroupId, OverviewDTO::setSubgroupId);
        binder.forField(getTagField()).
                withValidator(tagDTOList -> tagDTOList != null && tagDTOList.size() > 0,
                        "Please enter at least one tag!").bind(OverviewDTO::getTags, OverviewDTO::setTags);
        binder.readBean(overviewDTO);
        setContent(getParentLayout(overviewDTO, onSave, onDiscard));
    }

    private VerticalLayout getParentLayout(final OverviewDTO overviewDTO, final Runnable onSave,
                                           final Runnable onDiscard) {
        if (parentLayout == null) {
            parentLayout = new VerticalLayout();
            parentLayout.addComponents(getNameField(), getTextField(), getDateField(),
                    getSubgroupField(), getTagField(),
                    getButtonsLayout(overviewDTO, onSave, onDiscard));
        }
        return parentLayout;
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
            dateField = new OverviewDateField("Date:");
        }
        return dateField;
    }

    private OverviewSubgroupField getSubgroupField() {
        if (subgroupField == null) {
            subgroupField = new OverviewSubgroupField(groupService, subgroupService);
        }
        return subgroupField;
    }

    private OverviewTagField getTagField() {
        if (tagField == null) {
            tagField = new OverviewTagField(tagService);
            tagField.setCaption("Tags:");
        }
        return tagField;
    }

    private HorizontalLayout getButtonsLayout(final OverviewDTO overviewDTO,
                                              final Runnable onSave, final Runnable onDiscard) {
        if (buttonsLayout == null) {
            buttonsLayout = new HorizontalLayout();
            buttonsLayout.addComponents(getSaveButton(overviewDTO, onSave),
                    getCancelButton(onDiscard));
        }
        return buttonsLayout;
    }

    private Button getSaveButton(final OverviewDTO overviewDTO, final Runnable onSave) {
        if (saveButton == null) {
            saveButton = new Button("Save");
            saveButton.addClickListener(clickEvent -> {
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
            });
            saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        }
        return saveButton;
    }

    private Button getCancelButton(final Runnable onDiscard) {
        if (cancelButton == null) {
            cancelButton = new Button("Cancel");
            cancelButton.addClickListener(clickEvent -> onDiscard.run());
            cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        }
        return cancelButton;
    }

}
