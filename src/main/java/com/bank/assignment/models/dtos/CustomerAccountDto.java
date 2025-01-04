package com.bank.assignment.models.dtos;

import java.util.concurrent.locks.ReentrantLock;

public class CustomerAccountDto extends BaseDto {
  private String customerId;
  private String accountNumber;
  private String description;
  private ReentrantLock lock;

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ReentrantLock getLock() {
    return lock;
  }

  public void setLock(ReentrantLock lock) {
    this.lock = lock;
  }
}
