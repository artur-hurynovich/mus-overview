package by.hurynovich.mus_overview.vaadin.custom_field;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

import java.util.List;

public class OverviewTagField extends CustomField<List<String>> {

    public OverviewTagField(final String caption) {
        setCaption(caption);
    }

    @Override
    protected Component initContent() {
        return null;
    }

    @Override
    protected void doSetValue(final List<String> strings) {

    }

    @Override
    public List<String> getValue() {
        return null;
    }
}
