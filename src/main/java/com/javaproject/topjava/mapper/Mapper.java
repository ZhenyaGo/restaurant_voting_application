package com.javaproject.topjava.mapper;

import com.javaproject.topjava.model.NamedEntity;
import com.javaproject.topjava.to.NamedTo;

public interface Mapper<E extends NamedEntity, D extends NamedTo> {

    E toEntity(D dto);

    D toDto(E entity);
}
