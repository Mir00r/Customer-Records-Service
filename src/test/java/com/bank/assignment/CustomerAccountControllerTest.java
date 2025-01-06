package com.bank.assignment;

import com.bank.assignment.controllers.CustomerAccountController;
import com.bank.assignment.models.dtos.CustomerAccountDto;
import com.bank.assignment.models.entities.CustomerAccount;
import com.bank.assignment.models.mappers.CustomerAccountMapper;
import com.bank.assignment.services.CustomerAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CustomerAccountController.class, excludeAutoConfiguration = ErrorMvcAutoConfiguration.class)
public class CustomerAccountControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private CustomerAccountService service;

  @Mock
  private CustomerAccountMapper mapper;

  @BeforeEach
  void setUp() {
    // Initialize mocks and inject them into the controller
    CustomerAccountController controller = new CustomerAccountController(service, mapper);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void testGetAccountsWithoutSearch() throws Exception {
    // Mock service and mapper responses
    List<CustomerAccount> accounts = List.of(
      createTestAccount("CUST001", "ACC001", "First Account"),
      createTestAccount("CUST002", "ACC002", "Second Account")
    );
    Page<CustomerAccount> page = new PageImpl<>(accounts);

    List<CustomerAccountDto> accountsDto = List.of(
      this.createTestDtoAccount("CUST001", "ACC001", "First Account"),
      this.createTestDtoAccount("CUST002", "ACC002", "Second Account")
    );
    Page<CustomerAccountDto> pageDto = new PageImpl<>(accountsDto);

    when(service.findAll(eq(null), any(Pageable.class))).thenReturn(page);
    when(mapper.mapToPageDto(page)).thenReturn(pageDto);

    // Perform GET request and verify response
    mockMvc.perform(get("/api/v1/accounts")
        .param("page", "0")
        .param("size", "10"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content.length()").value(2))
      .andExpect(jsonPath("$.content[0].customerId").value("CUST001"))
      .andExpect(jsonPath("$.content[1].customerId").value("CUST002"));
  }

  @Test
  public void testUpdateDescription() throws Exception {
    // Mock service and mapper responses
    CustomerAccount account = createTestAccount("CUST001", "ACC001", "Old Description");
    CustomerAccountDto updatedDto =
      this.createTestDtoAccount("CUST001", "ACC001", "New Description");

    when(service.updateDescription(eq("ACC001"), eq("New Description"))).thenReturn(account);
    when(mapper.map(account)).thenReturn(updatedDto);

    // Perform PATCH request and verify response
    mockMvc.perform(patch("/api/v1/accounts/ACC001")
        .content("New Description")
        .contentType(MediaType.TEXT_PLAIN))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.customerId").value("CUST001"))
      .andExpect(jsonPath("$.accountNumber").value("ACC001"))
      .andExpect(jsonPath("$.description").value("New Description"));
  }

  // Utility method to create test accounts
  private CustomerAccount createTestAccount(String customerId, String accountNumber,
    String description) {
    CustomerAccount account = new CustomerAccount();
    account.setCustomerId(customerId);
    account.setAccountNumber(accountNumber);
    account.setDescription(description);
    return account;
  }

  private CustomerAccountDto createTestDtoAccount(String customerId, String accountNumber,
    String description) {
    CustomerAccountDto accountDto = new CustomerAccountDto();
    accountDto.setCustomerId(customerId);
    accountDto.setAccountNumber(accountNumber);
    accountDto.setDescription(description);
    return accountDto;
  }
}


