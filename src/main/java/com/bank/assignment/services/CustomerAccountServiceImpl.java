package com.bank.assignment.services;

import com.bank.assignment.models.entities.CustomerAccount;
import com.bank.assignment.repositories.CustomerAccountRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerAccountServiceImpl implements CustomerAccountService {

  private final CustomerAccountRepository repository;

  public CustomerAccountServiceImpl(CustomerAccountRepository repository) {
    this.repository = repository;
  }

  @Override
  public Page<CustomerAccount> findAll(String searchTerm, Pageable pageable) {
    if (searchTerm == null || searchTerm.isEmpty()) {
      return repository.findAll(pageable);
    }

    return repository.findAll((root, query, cb) -> {
      return cb.or(
        cb.like(root.get("customerId"), "%" + searchTerm + "%"),
        cb.like(root.get("accountNumber"), "%" + searchTerm + "%"),
        cb.like(root.get("description"), "%" + searchTerm + "%")
      );
    }, pageable);
  }

  @Override
  @Transactional
  @Retryable(value = OptimisticLockingFailureException.class, maxAttempts = 3)
  public CustomerAccount updateDescription(String accountNumber, String description) {
    CustomerAccount account = repository.findByAccountNumber(accountNumber)
      .orElseThrow(() -> new RuntimeException("Account not found"));

    try {
      account.getLock().lock();
      account.setDescription(description);
      return repository.save(account);
    } finally {
      account.getLock().unlock();
    }
  }
}
