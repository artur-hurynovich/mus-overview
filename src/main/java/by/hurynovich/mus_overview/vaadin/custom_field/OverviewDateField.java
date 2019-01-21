package by.hurynovich.mus_overview.vaadin.custom_field;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.VerticalLayout;

import java.time.LocalDate;

@org.springframework.stereotype.Component("overviewDateField")
public class OverviewDateField extends CustomField<LocalDate> {

    private VerticalLayout parentLayout;

    private DateField dateField;

    private LocalDate value;

    @Override
    protected Component initContent() {
        return getParentLayout();
    }

    @Override
    protected void doSetValue(final LocalDate localDate) {
        value = localDate;
        if (getValue() == null) {
            getDateField().clear();
        } else {
            getDateField().setValue(getValue());
        }
    }

    @Override
    public LocalDate getValue() {
        return value;
    }

    private VerticalLayout getParentLayout() {
        if (parentLayout == null) {
            parentLayout = new VerticalLayout();
            parentLayout.addComponent(getDateField());
        }
        return parentLayout;
    }

    private DateField getDateField() {
        if (dateField == null) {
            dateField = new DateField();
            dateField.setValue(getValue());
            dateField.addValueChangeListener(valueChangeEvent -> doSetValue(valueChangeEvent.getValue()));
        }
        return dateField;
    }

}
