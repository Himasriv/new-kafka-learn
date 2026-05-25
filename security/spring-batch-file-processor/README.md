# Spring Batch File Processor

This project runs a Spring Batch job every 5 minutes and processes only files that have not already been processed.

## What it does

- Polls `app.input-directory` every 5 minutes
- Checks table `processed_file` to decide whether a file name is already processed
- Processes only new files and stores the processed file record

## Configuration

Edit `src/main/resources/application.properties`:

- `app.input-directory=./input`
- `app.scheduler.cron=0 */5 * * * *`
- `app.scheduler.enabled=true`

Database defaults to file-based H2 at `./data/batchdb`.

## Run

```powershell
cd C:\Users\HimasriPeteti\workspace\security\spring-batch-file-processor
.\mvnw.cmd spring-boot:run
```

Drop files into the `input` folder and wait for the next 5-minute scheduler run.

## Test

```powershell
cd C:\Users\HimasriPeteti\workspace\security\spring-batch-file-processor
.\mvnw.cmd test
```

