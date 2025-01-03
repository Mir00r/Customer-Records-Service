package com.bank.assignment.repositories;

import com.bank.assignment.models.entities.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Long>,
  JpaSpecificationExecutor<CustomerAccount> {

  Optional<CustomerAccount> findByCustomerId(String customerId);

  Optional<CustomerAccount> findByAccountNumber(String accountNumber);
}
