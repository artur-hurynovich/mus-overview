package by.hurynovich.mus_overview.service;

import by.hurynovich.mus_overview.dto.AbstractDTO;

import java.util.List;

public interface DTOService<DTOClass extends AbstractDTO> {

    DTOClass save(final DTOClass dtoClass);

    DTOClass findOne(final long id);

    List<DTOClass> findAll();

    DTOClass update(final DTOClass dtoClass);

    void delete(final DTOClass dtoClass);

    long count();

}
