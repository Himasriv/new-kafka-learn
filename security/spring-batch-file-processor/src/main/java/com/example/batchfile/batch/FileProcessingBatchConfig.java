package com.example.batchfile.batch;

import com.example.batchfile.service.FileProcessingService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FileProcessingBatchConfig {

    @Bean
    public Job fileProcessingJob(JobRepository jobRepository, Step fileProcessingStep) {
        return new JobBuilder("fileProcessingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(fileProcessingStep)
                .build();
    }

    @Bean
    public Step fileProcessingStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FileProcessingService fileProcessingService
    ) {
        return new StepBuilder("fileProcessingStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    int processedFiles = fileProcessingService.processNewFiles();
                    contribution.getStepExecution()
                            .getExecutionContext()
                            .putInt("processedFiles", processedFiles);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}


