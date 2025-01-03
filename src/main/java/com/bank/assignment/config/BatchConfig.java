package com.bank.assignment.config;

import com.bank.assignment.batch.CustomerAccountProcessor;
import com.bank.assignment.batch.CustomerAccountWriter;
import com.bank.assignment.models.entities.CustomerAccount;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
public class BatchConfig {


  @Bean
  public Job importCustomerAccountJob(JobRepository jobRepository,
    Step step1) {
    return new JobBuilder("importCustomerAccountJob", jobRepository)
      .flow(step1)
      .end()
      .build();
  }

  @Bean
  public Step step1(JobRepository jobRepository,
    PlatformTransactionManager transactionManager,
    FlatFileItemReader<CustomerAccount> reader,
    CustomerAccountProcessor processor,
    CustomerAccountWriter writer) {
    return new StepBuilder("step1", jobRepository)
      .<CustomerAccount, CustomerAccount>chunk(10, transactionManager)
      .reader(reader)
      .processor(processor)
      .writer(writer)
      .build();
  }

  @Bean
  public FlatFileItemReader<CustomerAccount> reader() {
    FlatFileItemReader<CustomerAccount> reader = new FlatFileItemReader<>();
    reader.setResource(new ClassPathResource("data.txt"));
    reader.setLineMapper(new DefaultLineMapper<>() {{
      setLineTokenizer(new DelimitedLineTokenizer() {{
        setNames("customerId", "accountNumber", "description");
      }});
      setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
        setTargetType(CustomerAccount.class);
      }});
    }});
    return reader;
  }
}
