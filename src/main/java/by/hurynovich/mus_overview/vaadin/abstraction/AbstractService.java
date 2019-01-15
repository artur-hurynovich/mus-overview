package by.hurynovich.mus_overview.vaadin.abstraction;

import by.hurynovich.mus_overview.dto.AbstractDTO;

import java.util.List;

public interface AbstractService<DTOClass extends AbstractDTO> {

    DTOClass save(final DTOClass dtoClass) throws Exception;

    List<DTOClass> findAll();

    void delete(final DTOClass dtoClass);

    long count();

}
