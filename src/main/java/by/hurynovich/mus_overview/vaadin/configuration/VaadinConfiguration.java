package by.hurynovich.mus_overview.vaadin.configuration;

import by.hurynovich.mus_overview.dto.AbstractDTO;
import by.hurynovich.mus_overview.vaadin.annotation.GridColumn;
import by.hurynovich.mus_overview.vaadin.annotation.GridRenderer;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.TextRenderer;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Configuration
public class VaadinConfiguration {

    @Bean("grid")
    @ViewScope
    public <DTOClass extends AbstractDTO> Grid<DTOClass> getGrid(final DependencyDescriptor descriptor) {
        final Grid<DTOClass> grid = new Grid<>();
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        ((MultiSelectionModel<DTOClass>) grid.getSelectionModel()).
                setSelectAllCheckBoxVisibility(MultiSelectionModel.SelectAllCheckBoxVisibility.VISIBLE);
        final Class<?> type = descriptor.getResolvableType().getGeneric(0).resolve();
        setupColumns(grid, type);
        return grid;
    }

    private <DTOClass> void setupColumns(final Grid<DTOClass> grid, Class<?> dtoClass) {
        final List<Field> fields = new ArrayList<>();
        ReflectionUtils.doWithFields(dtoClass, fields::add);
        fields.stream().filter(field -> field.getAnnotation(GridColumn.class) != null).
                sorted(Comparator.comparing(this::getColumnPosition)).
                forEach(field -> {
                    final Grid.Column<DTOClass, ?> column = grid.addColumn(dto -> {
                        try {
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                            }
                            return field.get(dto);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Failed to retrieve property value!", e);
                        }
                    });
                    column.setCaption(getColumnCaption(field));
                    column.setId(field.getName());
                    setRenderer(field, column);
                });
    }

    private String getColumnCaption(final Field field) {
        return field.getAnnotation(GridColumn.class).caption();
    }

    private int getColumnPosition(final Field field) {
        return field.getAnnotation(GridColumn.class).position();
    }

    @SuppressWarnings("unchecked")
    private void setRenderer(final Field field, final Grid.Column column) {
        final GridRenderer gridRenderer = field.getAnnotation(GridRenderer.class);
        if (gridRenderer != null) {
            try {
                column.setRenderer((TextRenderer) Class.forName(gridRenderer.rendererClass()).newInstance());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Renderer class retrieving failed!", e);
            }
        }
    }

}
