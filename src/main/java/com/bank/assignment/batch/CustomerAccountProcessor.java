package com.bank.assignment.batch;

import com.bank.assignment.models.entities.CustomerAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CustomerAccountProcessor implements ItemProcessor<CustomerAccount, CustomerAccount> {

  private static final Logger logger = LoggerFactory.getLogger(CustomerAccountProcessor.class);

  @Override
  public CustomerAccount process(CustomerAccount account) throws Exception {
    try {
      // Trim strings and convert to uppercase where necessary
      account.setCustomerId(account.getCustomerId().trim().toUpperCase());
      account.setAccountNumber(account.getAccountNumber().trim().toUpperCase());

      if (account.getDescription() != null) {
        account.setDescription(account.getDescription().trim());
      }

      // Set audit timestamps
      LocalDateTime now = LocalDateTime.now();
      account.setCreatedAt(now);
      account.setUpdatedAt(now);

      // Log processed item
      logger.debug("Successfully processed account: {}", account.getAccountNumber());

      return account;
    } catch (Exception e) {
      logger.error("Error processing account: {}", account.getAccountNumber(), e);
      throw e;
    }
  }

  private boolean isValidAccount(CustomerAccount account) {
    // Add any additional validation rules
    return account.getCustomerId() != null &&
      account.getAccountNumber() != null &&
      account.getCustomerId().length() <= 50 &&
      account.getAccountNumber().length() <= 50;
  }
}
