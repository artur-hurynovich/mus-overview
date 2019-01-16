package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.dto.AbstractDTO;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AbstractDTOView<DTOClass extends AbstractDTO> extends CustomComponent implements View {

    private VerticalLayout parentLayout;

    @Autowired
    @Qualifier("grid")
    private Grid<DTOClass> grid;

    private ConfigurableFilterDataProvider<DTOClass, Void, ?> startDataProvider;

    private HorizontalLayout buttonsLayout;

    private Button addButton;

    private Button editButton;

    private Button deleteButton;

    public AbstractDTOView() {
        parentLayout = new VerticalLayout();
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        setupButtonsLayout();
        setupGrid();
        setupParentLayout();
        setCompositionRoot(getParentLayout());
    }

    protected void setStartDataProvider(final CallbackDataProvider<DTOClass, ?> startDataProvider) {
        this.startDataProvider = startDataProvider.withConfigurableFilter();
    }

    protected void setStartDataProvider(final ConfigurableFilterDataProvider<DTOClass, Void, ?> startDataProvider) {
        this.startDataProvider = startDataProvider;
    }

    private void setupButtonsLayout() {
        buttonsLayout = new HorizontalLayout();
        addButton = new Button("Add");
        editButton = new Button("Edit");
        editButton.setEnabled(false);
        deleteButton = new Button("Delete");
        deleteButton.setEnabled(false);
        buttonsLayout.addComponents(addButton, editButton, deleteButton);
    }

    private void setupGrid() {
        grid.addSelectionListener(selection -> {
            final int selectedItemsSize = selection.getAllSelectedItems().size();
            getEditButton().setEnabled(selectedItemsSize == 1);
            getDeleteButton().setEnabled(selectedItemsSize > 0);
        });
        grid.setDataProvider(startDataProvider);
        grid.getDataProvider().refreshAll();
    }

    private void setupParentLayout() {
        parentLayout.addComponents(getGrid(), getButtonsLayout());
    }

    protected VerticalLayout getParentLayout() {
        return parentLayout;
    }

    protected Grid<DTOClass> getGrid() {
        return grid;
    }

    protected HorizontalLayout getButtonsLayout() {
        return buttonsLayout;
    }

    protected Button getAddButton() {
        return addButton;
    }

    protected Button getEditButton() {
        return editButton;
    }

    protected Button getDeleteButton() {
        return deleteButton;
    }

    protected abstract Class<DTOClass> getDTOClass();

}
