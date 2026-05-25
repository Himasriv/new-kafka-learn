package com.example.batch.config;

import com.example.batch.model.CustomerRecord;
import java.util.Map;
import java.util.Locale;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    private static final String UPSERT_CUSTOMER_SQL =
            "MERGE INTO customer_output (id, name, email, city) KEY (id) " +
                    "VALUES (:id, :name, :email, :city)";

    @Bean
    public FlatFileItemReader<CustomerRecord> customerReader() {
        return new FlatFileItemReaderBuilder<CustomerRecord>()
                .name("customerReader")
                .resource(new ClassPathResource("input/customers.csv"))
                .delimited()
                .strict(false)
                .names("id", "name", "email", "city")
                .targetType(CustomerRecord.class)
                .linesToSkip(1)
                .build();
    }

    @Bean
    public ItemProcessor<CustomerRecord, CustomerRecord> customerProcessor() {
        return item -> {
            if (item.getId() == null
                    && isBlank(item.getName())
                    && isBlank(item.getEmail())
                    && isBlank(item.getCity())) {
                return null;
            }

            item.setName(toTitleCase(item.getName()));
            item.setEmail(item.getEmail().trim().toLowerCase(Locale.ROOT));
            item.setCity(item.getCity().trim().toUpperCase(Locale.ROOT));

            if (!item.getEmail().contains("@")) {
                throw new IllegalArgumentException("Invalid email for id=" + item.getId());
            }

            return item;
        };
    }

    @Bean
    public JdbcBatchItemWriter<CustomerRecord> customerWriter(NamedParameterJdbcTemplate jdbcTemplate) {
        return new JdbcBatchItemWriterBuilder<CustomerRecord>()
                .namedParametersJdbcTemplate(jdbcTemplate)
                .sql(UPSERT_CUSTOMER_SQL)
                .beanMapped()
                .build();
    }

    @Bean
    public Step importCustomersStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager,
                                    FlatFileItemReader<CustomerRecord> customerReader,
                                    ItemProcessor<CustomerRecord, CustomerRecord> customerProcessor,
                                    ItemWriter<CustomerRecord> customerWriter) {
        return new StepBuilder("importCustomersStep", jobRepository)
                .<CustomerRecord, CustomerRecord>chunk(3, transactionManager)
                .reader(customerReader)
                .processor(customerProcessor)
                .writer(customerWriter)
                .faultTolerant()
                .skip(FlatFileParseException.class)
                .skip(IllegalArgumentException.class)
                .skipLimit(10)
                .retry(CannotAcquireLockException.class)
                .retryLimit(3)
                .build();
    }

    @Bean
    public Step postImportSummaryStep(JobRepository jobRepository,
                                      PlatformTransactionManager transactionManager,
                                      JdbcTemplate jdbcTemplate) {
        return new StepBuilder("postImportSummaryStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    Integer count = jdbcTemplate.queryForObject(
                            "SELECT COUNT(*) FROM customer_output", Integer.class);

                    Map<String, Object> summaryRow = jdbcTemplate.queryForMap(
                            "SELECT MIN(id) AS min_id, MAX(id) AS max_id FROM customer_output");

                    System.out.printf(
                            "Summary step -> total=%d, minId=%s, maxId=%s%n",
                            count,
                            summaryRow.get("min_id"),
                            summaryRow.get("max_id"));

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Job importCustomersJob(JobRepository jobRepository,
                                  Step importCustomersStep,
                                  Step postImportSummaryStep) {
        return new JobBuilder("importCustomersJob", jobRepository)
                .start(importCustomersStep)
                .next(postImportSummaryStep)
                .build();
    }

    private String toTitleCase(String value) {
        String[] parts = value.trim().toLowerCase(Locale.ROOT).split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                result.append(part.substring(1));
            }
        }

        return result.toString();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}



