package com.github.zhenyago.votingsystem.mapper;

import com.github.zhenyago.votingsystem.model.NamedEntity;
import com.github.zhenyago.votingsystem.to.NamedTo;

public interface Mapper<E extends NamedEntity, D extends NamedTo> {

    E toEntity(D dto);

    D toDto(E entity);
}
