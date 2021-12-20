package com.javaproject.topjava.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.javaproject.topjava.model.NamedEntity;
import com.javaproject.topjava.to.NamedTo;

import java.util.Objects;

public abstract class AbstractMapper<E extends NamedEntity, D extends NamedTo> implements Mapper<E, D> {

    @Autowired
    ModelMapper mapper;

    private final Class<E> entityClass;
    private final Class<D> dtoClass;

    AbstractMapper(Class<E> entityClass, Class<D> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public E toEntity(D dto) {
        return Objects.isNull(dto)
                ? null
                : mapper.map(dto, entityClass);
    }

    @Override
    public D toDto(E entity) {
        return Objects.isNull(entity)
                ? null
                : mapper.map(entity, dtoClass);
    }
}
