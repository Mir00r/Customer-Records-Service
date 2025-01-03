package com.bank.assignment.batch;

import com.bank.assignment.models.entities.CustomerAccount;
import com.bank.assignment.repositories.CustomerAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerAccountWriter implements ItemWriter<CustomerAccount> {

  private static final Logger logger = LoggerFactory.getLogger(CustomerAccountWriter.class);

  private final CustomerAccountRepository repository;

  public CustomerAccountWriter(CustomerAccountRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public void write(Chunk<? extends CustomerAccount> chunk) throws Exception {
    List<CustomerAccount> successfulAccounts = new ArrayList<>();
    List<String> failedAccounts = new ArrayList<>();

    for (CustomerAccount account : chunk) {
      try {
        // Check if account already exists
        if (repository.findByAccountNumber(account.getAccountNumber()).isPresent()) {
          logger.warn("Account already exists: {}", account.getAccountNumber());
          failedAccounts.add(account.getAccountNumber());
          continue;
        }

        if (repository.findByCustomerId(account.getCustomerId()).isPresent()) {
          logger.warn("Customer ID already exists: {}", account.getCustomerId());
          failedAccounts.add(account.getAccountNumber());
          continue;
        }

        successfulAccounts.add(account);
      } catch (Exception e) {
        logger.error("Error processing account: {}", account.getAccountNumber(), e);
        failedAccounts.add(account.getAccountNumber());
      }
    }

    // Save all successful accounts in a single batch
    if (!successfulAccounts.isEmpty()) {
      try {
        repository.saveAll(successfulAccounts);
        logger.info("Successfully saved {} accounts", successfulAccounts.size());
      } catch (Exception e) {
        logger.error("Error saving batch of accounts", e);
        throw e;
      }
    }

    // Log failed accounts
    if (!failedAccounts.isEmpty()) {
      logger.warn("Failed to process {} accounts: {}",
        failedAccounts.size(),
        String.join(", ", failedAccounts));
    }
  }
}
