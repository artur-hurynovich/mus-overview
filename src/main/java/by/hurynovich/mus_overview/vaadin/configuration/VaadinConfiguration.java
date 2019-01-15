package by.hurynovich.mus_overview.vaadin.configuration;

import by.hurynovich.mus_overview.dto.AbstractDTO;
import by.hurynovich.mus_overview.vaadin.annotation.GridColumn;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Grid;
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

    @Bean("abstractionOverviewGrid")
    @ViewScope
    public <DTOClass extends AbstractDTO> Grid<DTOClass> getGrid(
            final DependencyDescriptor descriptor) {
        Grid<DTOClass> grid = new Grid<>();
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        final Class<?> type = descriptor.getResolvableType().getGeneric(0).resolve();
        setupColumns(grid, type);
        return grid;
    }

    private <DTOClass> void setupColumns(final Grid<DTOClass> grid, Class<?> dtoClass) {
        final List<Field> fields = new ArrayList<>();
        ReflectionUtils.doWithFields(dtoClass, fields::add);
        fields.stream().filter(field -> field.getAnnotation(GridColumn.class) != null).
                sorted(Comparator.comparing(this::retrieveColumnPosition)).
                forEach(field -> {
                    final Grid.Column<DTOClass, ?> column = grid.addColumn(dto -> {
                        try {
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                            }
                            return field.get(dto);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Failed to retrieve property value", e);
                        }
                    });
                    column.setCaption(retrieveColumnCaption(field));
                });
    }

    private String retrieveColumnCaption(final Field field) {
        return field.getAnnotation(GridColumn.class).caption();
    }

    private int retrieveColumnPosition(final Field field) {
        return field.getAnnotation(GridColumn.class).position();
    }

}
