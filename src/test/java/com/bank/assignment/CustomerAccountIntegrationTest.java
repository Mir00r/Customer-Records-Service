package com.bank.assignment;

import com.bank.assignment.models.entities.CustomerAccount;
import com.bank.assignment.repositories.CustomerAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerAccountIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CustomerAccountRepository repository;

  @BeforeEach
  public void setup() {
    // Clean database and insert test data
    repository.deleteAll();

    // Add test accounts
    repository.save(createTestAccount("CUST001", "ACC001", "First Account"));
    repository.save(createTestAccount("CUST002", "ACC002", "Second Account"));
    repository.save(createTestAccount("CUST003", "ACC003", "Third Account"));
  }

  @Test
  public void testGetAllAccounts() throws Exception {
    mockMvc.perform(get("/api/v1/accounts"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content.length()").value(3))
      .andExpect(jsonPath("$.totalElements").value(3));
  }

  @Test
  public void testSearchAccounts() throws Exception {
    mockMvc.perform(get("/api/v1/accounts")
        .param("search", "CUST001"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content.length()").value(1))
      .andExpect(jsonPath("$.content[0].customerId").value("CUST001"));
  }

  @Test
  public void testPaginatedResults() throws Exception {
    mockMvc.perform(get("/api/v1/accounts")
        .param("page", "0")
        .param("size", "2"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content.length()").value(2))
      .andExpect(jsonPath("$.totalElements").value(3))
      .andExpect(jsonPath("$.totalPages").value(2));
  }

  private CustomerAccount createTestAccount(String customerId, String accountNumber,
    String description) {
    CustomerAccount account = new CustomerAccount();
    account.setCustomerId(customerId);
    account.setAccountNumber(accountNumber);
    account.setDescription(description);
    return account;
  }
}
