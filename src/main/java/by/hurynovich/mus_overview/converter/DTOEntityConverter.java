package by.hurynovich.mus_overview.converter;

public interface DTOEntityConverter<T, B> {
    T convertToDTO(final B entity);
    B convertToEntity(final T dto);
}
