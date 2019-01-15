package by.hurynovich.mus_overview.vaadin.abstraction;

import by.hurynovich.mus_overview.dto.AbstractDTO;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AbstractDTOView<DTOClass extends AbstractDTO> extends CustomComponent implements View {

    @Autowired
    @Qualifier("abstractionOverviewService")
    private AbstractService<DTOClass> service;

    private VerticalLayout parentLayout;

    @Autowired
    @Qualifier("abstractionOverviewGrid")
    private Grid<DTOClass> grid;

    private HorizontalLayout buttonsLayout;

    private Button addButton;

    private Button deleteButton;

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        setCompositionRoot(getParentLayout());
    }

    protected abstract Class<DTOClass> getEntityClass();

    private VerticalLayout getParentLayout() {
        if (parentLayout == null) {
            parentLayout = new VerticalLayout();
            setupGrid();
            parentLayout.addComponents(getGrid(), getButtonsLayout());
        }
        return parentLayout;
    }

    private void setupGrid() {
        grid.setDataProvider(getDataProvider());
        grid.getDataProvider().refreshAll();
        grid.addSelectionListener(selection -> {
            final int selectedItemsSize = selection.getAllSelectedItems().size();
            addButton.setEnabled(selectedItemsSize == 1);
            deleteButton.setEnabled(selectedItemsSize > 0);
        });
    }

    private Grid<DTOClass> getGrid() {
        return grid;
    }

    private CallbackDataProvider<DTOClass, String> getDataProvider() {
        return new CallbackDataProvider<>(
                query -> service.findAll().stream(),
                query -> (int) service.count()
        );
    }

    private HorizontalLayout getButtonsLayout() {
        if (buttonsLayout == null) {
            buttonsLayout = new HorizontalLayout();
            buttonsLayout.addComponents(getAddButton(), getDeleteButton());
        }
        return buttonsLayout;
    }

    private Button getAddButton() {
        if (addButton == null) {
            addButton = new Button("Add");
        }
        return addButton;
    }

    private Button getDeleteButton() {
        if (deleteButton == null) {
            deleteButton = new Button("Delete");
        }
        return deleteButton;
    }

}
