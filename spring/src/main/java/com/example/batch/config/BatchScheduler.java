package com.example.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job importCustomersJob;

    public BatchScheduler(JobLauncher jobLauncher,
                          @Qualifier("importCustomersJob") Job importCustomersJob) {
        this.jobLauncher = jobLauncher;
        this.importCustomersJob = importCustomersJob;
    }

    @Scheduled(cron = "${app.batch.cron}")
    public void runScheduledJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("scheduledAt", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(importCustomersJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException
                 | JobRestartException
                 | JobInstanceAlreadyCompleteException ex) {
            System.out.println("Skipping scheduled run: " + ex.getMessage());
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to run scheduled batch job", ex);
        }
    }
}

