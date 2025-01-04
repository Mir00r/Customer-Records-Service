package com.bank.assignment.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobTrigger implements CommandLineRunner {

  private final JobLauncher jobLauncher;

  private final Job job;

  @Autowired
  public JobTrigger(JobLauncher jobLauncher, Job job) {
    this.jobLauncher = jobLauncher;
    this.job = job;
  }

  @Override
  public void run(String... args) throws Exception {
    jobLauncher.run(job, new org.springframework.batch.core.JobParameters());
  }
}
