package com.bank.assignment.services;

import com.bank.assignment.models.entities.CustomerAccount;
import com.bank.assignment.repositories.CustomerAccountRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // Marks this class as a service layer component.
@Transactional // Ensures database transactions are managed automatically.
public class CustomerAccountServiceImpl implements CustomerAccountService {

  private final CustomerAccountRepository repository;

  // Constructor-based dependency injection for the repository.
  public CustomerAccountServiceImpl(CustomerAccountRepository repository) {
    this.repository = repository;
  }

  @Override
  public Page<CustomerAccount> findAll(String searchTerm, Pageable pageable) {
    // Fetch accounts with optional search criteria.
    if (searchTerm == null || searchTerm.isEmpty()) {
      return this.repository.findAll(pageable);
    }

    // Dynamic query for searching by multiple fields.
    return this.repository.findAll((root, query, cb) -> cb.or(
      cb.like(root.get("customerId"), "%" + searchTerm + "%"),
      cb.like(root.get("accountNumber"), "%" + searchTerm + "%"),
      cb.like(root.get("description"), "%" + searchTerm + "%")
    ), pageable);
  }

  @Override
  @Transactional
  @Retryable(value = OptimisticLockingFailureException.class, maxAttempts = 3)
  // Retry mechanism for concurrency handling.
  public CustomerAccount updateDescription(String accountNumber, String description) {
    CustomerAccount account = this.repository.findByAccountNumber(accountNumber)
      .orElseThrow(
        () -> new RuntimeException("Account not found")); // Exception if account doesn't exist.

    try {
      account.getLock().lock(); // Explicit locking to ensure thread safety.
      account.setDescription(description); // Update the description field.
      return this.repository.save(account); // Save the updated entity.
    } finally {
      account.getLock().unlock(); // Ensure lock is released in case of exceptions.
    }
  }
}

