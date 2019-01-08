package by.hurynovich.mus_overview.vaadin.custom_field;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.VerticalLayout;

import java.time.LocalDate;

public class OverviewDateField extends CustomField<LocalDate> {

    private LocalDate value;

    private final VerticalLayout parentLayout;

    private final DateField dateField;

    public OverviewDateField(final String caption) {
        setCaption(caption);
        parentLayout = new VerticalLayout();
        dateField = new DateField();
    }

    @Override
    protected Component initContent() {
        return getParentLayout();
    }

    @Override
    protected void doSetValue(final LocalDate localDate) {
        value = localDate;
    }

    @Override
    public LocalDate getValue() {
        return value;
    }

    private VerticalLayout getParentLayout() {
        parentLayout.addComponent(getDateField());
        return parentLayout;
    }

    private DateField getDateField() {
        dateField.setValue(getValue());
        dateField.addValueChangeListener(valueChangeEvent -> doSetValue(dateField.getValue()));
        return dateField;
    }

}
