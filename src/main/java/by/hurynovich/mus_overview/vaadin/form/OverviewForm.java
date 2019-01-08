package by.hurynovich.mus_overview.vaadin.form;

import by.hurynovich.mus_overview.dto.OverviewDTO;
import by.hurynovich.mus_overview.vaadin.custom_field.OverviewDateField;
import by.hurynovich.mus_overview.vaadin.custom_field.TagListField;
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

    @PropertyId("title")
    private final TextField titleField;

    @PropertyId("text")
    private final TextArea textField;

    @PropertyId("date")
    private final OverviewDateField dateField;

    @PropertyId("tags")
    private final TagListField tagField;

    private final VerticalLayout parentLayout;

    private final HorizontalLayout buttonsLayout;

    private final Button saveButton;

    private final Button cancelButton;

    private final Binder<OverviewDTO> binder;

    public OverviewForm(final OverviewDTO overviewDTO, final Runnable onSave) {
        titleField = new TextField("Title");
        textField = new TextArea("Text");
        dateField = new OverviewDateField("Date");
        tagField = new TagListField("Tags");
        parentLayout = new VerticalLayout();
        buttonsLayout = new HorizontalLayout();
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        binder = new Binder<>(OverviewDTO.class);
        binder.setBean(overviewDTO);
        binder.bindInstanceFields(this);
        setContent(getParentLayout(overviewDTO, onSave));
    }

    private VerticalLayout getParentLayout(final OverviewDTO overviewDTO, final Runnable onSave) {
        parentLayout.
                addComponents(getTitleField(), getTextField(), getDateField(), getTagField(),
                        getButtonsLayout(overviewDTO, onSave));
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

    private TagListField getTagField() {
        return tagField;
    }

    private HorizontalLayout getButtonsLayout(final OverviewDTO overviewDTO, final Runnable onSave) {
        buttonsLayout.addComponents(getSaveButton(overviewDTO, onSave), getCancelButton());
        return buttonsLayout;
    }

    private Button getSaveButton(final OverviewDTO oldOverviewDTO, final Runnable onSave) {
        saveButton.addClickListener(clickEvent -> {
            try {
                final OverviewDTO newOverviewDTO = binder.getBean();
                binder.writeBean(newOverviewDTO);
                onSave.run();
            } catch (final ValidationException e) {
                Notification.show("Warning!", "Form is not valid!",
                        Notification.Type.WARNING_MESSAGE);
            }
        });
        return saveButton;
    }

    private Button getCancelButton() {
        return cancelButton;
    }

}
