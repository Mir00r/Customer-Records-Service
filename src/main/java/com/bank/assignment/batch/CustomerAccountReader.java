//package com.bank.assignment.batch;
//
//import com.bank.assignment.models.entities.CustomerAccount;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomerAccountReader extends FlatFileItemReader<CustomerAccount> {
//
//  public CustomerAccountReader(Resource inputResource) {
//    // Set the input file resource
//    setResource(inputResource);
//
//    // Skip the header line if present
//    setLinesToSkip(1);
//
//    // Configure how each line should be parsed
//    setLineMapper(new DefaultLineMapper<>() {{
//      setLineTokenizer(new DelimitedLineTokenizer() {{
//        setNames("customerId", "accountNumber", "description");
//        setDelimiter(",");
//      }});
//
//      setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
//        setTargetType(CustomerAccount.class);
//      }});
//    }});
//  }
//
//  // Custom validation method
//  @Override
//  protected CustomerAccount doRead() throws Exception {
//    CustomerAccount account = super.doRead();
//    if (account != null) {
//      validateAccount(account);
//    }
//    return account;
//  }
//
//  private void validateAccount(CustomerAccount account) throws Exception {
//    if (account.getCustomerId() == null || account.getCustomerId().trim().isEmpty()) {
//      throw new Exception("Customer ID cannot be empty");
//    }
//    if (account.getAccountNumber() == null || account.getAccountNumber().trim().isEmpty()) {
//      throw new Exception("Account number cannot be empty");
//    }
//  }
//}
