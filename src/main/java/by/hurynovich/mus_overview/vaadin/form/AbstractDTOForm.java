package by.hurynovich.mus_overview.vaadin.form;

import by.hurynovich.mus_overview.dto.AbstractDTO;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractDTOForm<DTOClass extends AbstractDTO> extends Panel {

    private VerticalLayout parentLayout;

    private HorizontalLayout buttonsLayout;

    private Button saveButton;

    private Button cancelButton;

    private Binder<DTOClass> binder;

    public AbstractDTOForm() {
        setupBinder();
        setupParentLayout();
        setupButtonsLayout();
    }

    private void setupParentLayout() {
        parentLayout = new VerticalLayout();
    }

    public VerticalLayout getParentLayout() {
        return parentLayout;
    }

    private void setupBinder() {
        binder = new Binder<>();
    }

    public Binder<DTOClass> getBinder() {
        return binder;
    }

    private void setupButtonsLayout() {
        buttonsLayout = new HorizontalLayout();
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        buttonsLayout.addComponents(saveButton, cancelButton);
    }

    protected HorizontalLayout getButtonsLayout() {
        return buttonsLayout;
    }

    protected Button getSaveButton() {
        return saveButton;
    }

    protected Button getCancelButton() {
        return cancelButton;
    }

    abstract protected Class<DTOClass> getDTOClass();

}
