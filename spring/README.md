# Spring Batch Demo Project

This project is a beginner-friendly Spring Batch application built with Spring Boot and Maven.

## What it does

The batch flow now includes:

1. reads customer data from `src/main/resources/input/customers.csv`
2. normalizes the data in a processor
3. writes the processed records to `customer_output` table in H2
4. executes a second step that prints a summary from the database

It also includes:

- skip/retry fault tolerance on the import step
- cron-based scheduling with `@Scheduled`

## Tech stack

- Java 17+
- Spring Boot 3
- Spring Batch 5
- Maven
- H2 in-memory database for Spring Batch metadata and output table

## Project structure

- `pom.xml` - Maven build configuration
- `src/main/java/com/example/batch/BatchApplication.java` - application entry point
- `src/main/java/com/example/batch/config/BatchConfig.java` - reader, processor, DB writer, and 2-step job
- `src/main/java/com/example/batch/config/BatchScheduler.java` - cron-based job trigger
- `src/main/java/com/example/batch/model/CustomerRecord.java` - simple domain model
- `src/main/resources/schema.sql` - creates `customer_output` table
- `src/main/resources/input/customers.csv` - sample input data
- `src/test/java/com/example/batch/BatchApplicationTests.java` - integration test for the batch job

## Run the application

```powershell
mvn spring-boot:run
```

By default, the scheduler runs every 5 seconds (`app.batch.cron=0/5 * * * * *`).

## Run the tests

```powershell
mvn test
```

## Expected output

You should see lines similar to:

```text
Summary step -> total=5, minId=1, maxId=5
```

## Notes

- `importCustomersJob` has two steps: `importCustomersStep` and `postImportSummaryStep`.
- The import step uses chunk processing with a chunk size of `3`.
- Skip policy: malformed CSV rows and invalid email rows are skipped (limit `10`).
- Retry policy: transient DB deadlock errors are retried (limit `3`).
- Automatic startup is disabled (`spring.batch.job.enabled=false`), and scheduler triggers the job.

