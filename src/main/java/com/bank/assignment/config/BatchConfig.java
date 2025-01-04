package com.bank.assignment.config;

import com.bank.assignment.batch.CustomerAccountProcessor;
import com.bank.assignment.batch.CustomerAccountWriter;
import com.bank.assignment.models.entities.CustomerAccount;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing // Enables Spring Batch processing.
public class BatchConfig {

  @Bean
  public Job importCustomerAccountJob(JobRepository jobRepository, Step step1) {
    // Configures the batch job with a single step.
    return new JobBuilder("importCustomerAccountJob", jobRepository)
      .flow(step1) // Defines the step to execute.
      .end()
      .build();
  }

  @Bean
  public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
    FlatFileItemReader<CustomerAccount> reader,
    CustomerAccountProcessor processor,
    CustomerAccountWriter writer) {
    // Configures the step with reader, processor, and writer.
    return new StepBuilder("step1", jobRepository)
      .<CustomerAccount, CustomerAccount>chunk(10,
        transactionManager) // Processes 10 items at a time.
      .reader(reader) // Reads input data.
      .processor(processor) // Processes data.
      .writer(writer) // Writes processed data to the database.
      .build();
  }

  @Bean
  public FlatFileItemReader<CustomerAccount> reader() {
    // Reads data from a text file line by line.
    FlatFileItemReader<CustomerAccount> reader = new FlatFileItemReader<>();
    reader.setResource(new ClassPathResource("data.txt")); // Source file location.
    reader.setLineMapper(new DefaultLineMapper<>() {{
      setLineTokenizer(new DelimitedLineTokenizer() {{
        setNames("customerId", "accountNumber", "description"); // Defines column names.
      }});
      setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
        setTargetType(CustomerAccount.class); // Maps columns to fields.
      }});
    }});
    return reader;
  }
}

