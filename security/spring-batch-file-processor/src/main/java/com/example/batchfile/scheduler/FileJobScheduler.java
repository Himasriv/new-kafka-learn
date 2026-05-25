package com.example.batchfile.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.scheduler.enabled", havingValue = "true", matchIfMissing = true)
public class FileJobScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileJobScheduler.class);

    private final JobLauncher jobLauncher;
    private final Job fileProcessingJob;

    public FileJobScheduler(JobLauncher jobLauncher, Job fileProcessingJob) {
        this.jobLauncher = jobLauncher;
        this.fileProcessingJob = fileProcessingJob;
    }

    @Scheduled(cron = "${app.scheduler.cron:0 */5 * * * *}")
    public void launchJob() {
        JobParameters params = new JobParametersBuilder()
                .addLong("triggerTime", System.currentTimeMillis())
                .toJobParameters();

        try {
            jobLauncher.run(fileProcessingJob, params);
            LOGGER.info("Scheduled fileProcessingJob run submitted");
        } catch (Exception ex) {
            LOGGER.error("Failed to execute fileProcessingJob", ex);
        }
    }
}

