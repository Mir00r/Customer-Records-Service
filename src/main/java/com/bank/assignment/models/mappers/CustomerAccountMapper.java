package com.bank.assignment.models.mappers;

import com.bank.assignment.models.dtos.CustomerAccountDto;
import com.bank.assignment.models.entities.CustomerAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component // Marks this class as a Spring-managed component for mapping entities to DTOs and vice versa.
public class CustomerAccountMapper implements BaseMapper<CustomerAccount, CustomerAccountDto> {

  @Override
  public CustomerAccountDto map(CustomerAccount entity) {
    CustomerAccountDto dto = new CustomerAccountDto();

    // Map all fields from entity to DTO.
    dto.setId(entity.getId());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());
    dto.setVersion(entity.getVersion());
    dto.setLock(entity.getLock());
    dto.setCustomerId(entity.getCustomerId());
    dto.setDescription(entity.getDescription());
    dto.setAccountNumber(entity.getAccountNumber());

    return dto;
  }

  @Override
  public CustomerAccount map(CustomerAccountDto dto, CustomerAccount exEntity) {
    CustomerAccount entity = exEntity == null ? new CustomerAccount() : exEntity;

    // Map DTO fields back to the entity.
    entity.setCustomerId(dto.getCustomerId());
    entity.setDescription(dto.getDescription());
    entity.setAccountNumber(dto.getAccountNumber());

    return entity;
  }

  public Page<CustomerAccountDto> mapToPageDto(Page<CustomerAccount> entities) {
    // Convert a page of entities into a page of DTOs.
    List<CustomerAccountDto> dtoList = entities.getContent().stream()
      .map(this::map) // Map each entity to DTO.
      .collect(Collectors.toList());
    return new PageImpl<>(dtoList, entities.getPageable(), entities.getTotalElements());
  }
}

