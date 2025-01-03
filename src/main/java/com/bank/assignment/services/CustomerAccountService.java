package com.bank.assignment.services;

import com.bank.assignment.models.entities.CustomerAccount;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerAccountService {

  Page<CustomerAccount> findAll(String searchTerm, Pageable pageable);

  CustomerAccount updateDescription(String accountNumber, String description) throws
    OptimisticLockingFailureException;
}
