package org.example.dto.mapper;

public interface DefaultMapper<D, E> {
    D toDto(E entity);

    E toEntity(D dto);
}
