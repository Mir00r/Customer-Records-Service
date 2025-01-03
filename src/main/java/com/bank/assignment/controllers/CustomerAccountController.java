package com.bank.assignment.controllers;

import com.bank.assignment.models.entities.CustomerAccount;
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

@RestController
@RequestMapping("/api/v1/accounts")
public class CustomerAccountController {

  private final CustomerAccountService service;

  public CustomerAccountController(CustomerAccountService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<Page<CustomerAccount>> getAccounts(
    @RequestParam(required = false) String search,
    Pageable pageable) {
    return ResponseEntity.ok(service.findAll(search, pageable));
  }

  @PatchMapping("/{accountNumber}/description")
  public ResponseEntity<CustomerAccount> updateDescription(
    @PathVariable String accountNumber,
    @RequestBody String description) {
    return ResponseEntity.ok(service.updateDescription(accountNumber, description));
  }
}
