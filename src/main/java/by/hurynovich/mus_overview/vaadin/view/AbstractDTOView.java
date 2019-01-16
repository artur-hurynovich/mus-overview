package by.hurynovich.mus_overview.vaadin.view;

import by.hurynovich.mus_overview.dto.AbstractDTO;
import by.hurynovich.mus_overview.service.IDTOService;
import com.vaadin.data.provider.CallbackDataProvider;
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

    private HorizontalLayout buttonsLayout;

    private Button addButton;

    private Button editButton;

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
        grid.addSelectionListener(selection -> {
            final int selectedItemsSize = selection.getAllSelectedItems().size();
            getEditButton().setEnabled(selectedItemsSize == 1);
            getDeleteButton().setEnabled(selectedItemsSize > 0);
        });
    }

    private Grid<DTOClass> getGrid() {
        return grid;
    }

    private HorizontalLayout getButtonsLayout() {
        if (buttonsLayout == null) {
            buttonsLayout = new HorizontalLayout();
            buttonsLayout.addComponents(getAddButton(), getEditButton(), getDeleteButton());
        }
        return buttonsLayout;
    }

    private Button getAddButton() {
        if (addButton == null) {
            addButton = new Button("Add");
        }
        return addButton;
    }

    private Button getEditButton() {
        if (editButton == null) {
            editButton = new Button("Edit");
        }
        return editButton;
    }

    private Button getDeleteButton() {
        if (deleteButton == null) {
            deleteButton = new Button("Delete");
        }
        return deleteButton;
    }

}
