package by.hurynovich.mus_overview.vaadin.abstraction;

import by.hurynovich.mus_overview.dto.impl.OverviewDTO;
import com.vaadin.spring.annotation.SpringView;

@SpringView(name = "")
public class OverviewDTOView extends AbstractDTOView<OverviewDTO> {

    @Override
    protected Class<OverviewDTO> getEntityClass() {
        return OverviewDTO.class;
    }

}
