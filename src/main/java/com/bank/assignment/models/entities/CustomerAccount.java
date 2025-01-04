package com.bank.assignment.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

@Entity
@Table(name = "customer_accounts")
public class CustomerAccount extends BaseEntity {

  @Column(unique = true)
  private String customerId;

  @Column(unique = true)
  private String accountNumber;

  private String description;

  @Transient
  private final ReentrantLock lock = new ReentrantLock();

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

}
