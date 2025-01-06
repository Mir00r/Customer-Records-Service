package com.bank.assignment;

import com.bank.assignment.models.entities.CustomerAccount;
import com.bank.assignment.repositories.CustomerAccountRepository;
import com.bank.assignment.services.CustomerAccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerAccountServiceTest {

  @Mock
  private CustomerAccountRepository repository;

  @InjectMocks
  private CustomerAccountServiceImpl service;

  @Test
  public void testUpdateDescription() {
    CustomerAccount account = new CustomerAccount();
    account.setAccountNumber("ACC123");
    account.setDescription("Old description");

    when(repository.findByAccountNumber("ACC123")).thenReturn(Optional.of(account));
    when(repository.save(any(CustomerAccount.class))).thenAnswer(i -> i.getArguments()[0]);

    CustomerAccount updated = service.updateDescription("ACC123", "New description");
    assertEquals("New description", updated.getDescription());
  }

  @Test
  public void testConcurrentUpdate() throws InterruptedException {
    CustomerAccount account = new CustomerAccount();
    account.setAccountNumber("ACC123");
    account.setDescription("Initial");

    when(repository.findByAccountNumber("ACC123")).thenReturn(Optional.of(account));
    when(repository.save(any(CustomerAccount.class))).thenAnswer(i -> i.getArguments()[0]);

    CountDownLatch latch = new CountDownLatch(2);
    AtomicInteger successCount = new AtomicInteger(0);

    Runnable updateTask = () -> {
      try {
        service.updateDescription("ACC123", "Updated-" + Thread.currentThread().getId());
        successCount.incrementAndGet();
      } catch (Exception e) {
        // Expected for some threads due to optimistic locking
      } finally {
        latch.countDown();
      }
    };

    new Thread(updateTask).start();
    new Thread(updateTask).start();

    latch.await(5, TimeUnit.SECONDS);
    assertEquals(1, successCount.get(), "Only one update should succeed");
  }
}

