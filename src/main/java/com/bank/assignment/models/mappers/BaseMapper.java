package com.bank.assignment.models.mappers;

import com.bank.assignment.models.dtos.BaseDto;
import com.bank.assignment.models.entities.BaseEntity;

public interface BaseMapper<T extends BaseEntity, S extends BaseDto> {

  S map(T entity);

  T map(S dto, T exEntity);
}
