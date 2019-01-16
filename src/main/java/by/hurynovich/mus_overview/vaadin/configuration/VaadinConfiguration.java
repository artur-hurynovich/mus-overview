package by.hurynovich.mus_overview.vaadin.configuration;

import by.hurynovich.mus_overview.dto.AbstractDTO;
import by.hurynovich.mus_overview.dto.impl.GroupDTO;
import by.hurynovich.mus_overview.dto.impl.OverviewDTO;
import by.hurynovich.mus_overview.dto.impl.SubgroupDTO;
import by.hurynovich.mus_overview.dto.impl.UserDTO;
import by.hurynovich.mus_overview.vaadin.annotation.GridColumn;
import by.hurynovich.mus_overview.vaadin.annotation.GridRenderer;
import by.hurynovich.mus_overview.vaadin.form.AbstractDTOForm;
import by.hurynovich.mus_overview.vaadin.form.impl.GroupForm;
import by.hurynovich.mus_overview.vaadin.form.impl.OverviewForm;
import by.hurynovich.mus_overview.vaadin.form.impl.SignInForm;
import by.hurynovich.mus_overview.vaadin.form.impl.SignUpForm;
import by.hurynovich.mus_overview.vaadin.form.impl.SubgroupForm;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Grid;
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

    @Bean("groupForm")
    @ViewScope
    public AbstractDTOForm<GroupDTO> getGroupForm(final DependencyDescriptor descriptor) {
        final AbstractDTOForm<GroupDTO> form = new GroupForm();
        /*final Binder<GroupDTO> binder = form.getBinder();
        final Class<?> type = descriptor.getResolvableType().getGeneric(0).resolve();
        setupBinder(binder, type);*/
        return form;
    }

    @Bean("subgroupForm")
    @ViewScope
    public AbstractDTOForm<SubgroupDTO> getSubgroupForm() {
        return new SubgroupForm();
    }

    @Bean("overviewForm")
    @ViewScope
    public AbstractDTOForm<OverviewDTO> getOverviewForm() {
        return new OverviewForm();
    }

    @Bean("signUpForm")
    @ViewScope
    public AbstractDTOForm<UserDTO> getSignUpForm() {
        return new SignUpForm();
    }

    @Bean("signInForm")
    @ViewScope
    public AbstractDTOForm<UserDTO> getSignInForm() {
        return new SignInForm();
    }

    /*private <DTOClass> void setupBinder(final Binder<DTOClass> binder, Class<?> dtoClass) {
        final List<Field> fields = new ArrayList<>();
        ReflectionUtils.doWithFields(dtoClass, fields::add);
        fields.forEach(field -> {
                    binder.bind(field, );
                });
    }*/

}
