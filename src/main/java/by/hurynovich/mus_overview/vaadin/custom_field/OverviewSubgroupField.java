package by.hurynovich.mus_overview.vaadin.custom_field;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

public class OverviewSubgroupField extends CustomField<Long> {

    public OverviewSubgroupField(final String caption) {
        setCaption(caption);
    }

    @Override
    protected Component initContent() {
        return null;
    }

    @Override
    protected void doSetValue(final Long aLong) {

    }

    @Override
    public Long getValue() {
        return null;
    }
}
