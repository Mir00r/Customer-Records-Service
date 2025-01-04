package com.bank.assignment.controllers;

import com.bank.assignment.models.dtos.CustomerAccountDto;
import com.bank.assignment.models.entities.CustomerAccount;
import com.bank.assignment.models.mappers.CustomerAccountMapper;
import com.bank.assignment.routing.ApiRoutes;
import com.bank.assignment.services.CustomerAccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Marks this class as a REST controller, exposing endpoints for client interactions.
@RestController
public class CustomerAccountController {

  private final CustomerAccountService service;
  private final CustomerAccountMapper customerAccountMapper;

  // Constructor-based dependency injection for the service and mapper components.
  public CustomerAccountController(CustomerAccountService service,
    CustomerAccountMapper customerAccountMapper) {
    this.service = service;
    this.customerAccountMapper = customerAccountMapper;
  }

  @GetMapping(ApiRoutes.GET_ACCOUNTS)
  // Endpoint to fetch paginated accounts, with optional search criteria.
  public ResponseEntity<Page<CustomerAccountDto>> getAccounts(
    @RequestParam(required = false) String search, // Optional search parameter.
    Pageable pageable) { // Handles pagination and sorting automatically.
    return ResponseEntity.ok(
      this.customerAccountMapper.mapToPageDto(
        service.findAll(search, pageable))); // Maps entities to DTOs.
  }

  @PatchMapping(ApiRoutes.PATCH_DESCRIPTION) // Endpoint to update the description of an account.
  public ResponseEntity<CustomerAccountDto> updateDescription(
    @PathVariable String accountNumber, // Account identifier.
    @RequestBody String description) { // New description payload.
    return ResponseEntity.ok(
      this.customerAccountMapper.map(service.updateDescription(accountNumber,
        description))); // Handles concurrency and updates the record.
  }
}

