package com.example.batchfile.service;

import com.example.batchfile.entity.ProcessedFile;
import com.example.batchfile.repository.ProcessedFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileProcessingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessingService.class);

    private final ProcessedFileRepository processedFileRepository;

    @Value("${app.input-directory:./input}")
    private String inputDirectory;

    public FileProcessingService(ProcessedFileRepository processedFileRepository) {
        this.processedFileRepository = processedFileRepository;
    }

    public int processNewFiles() throws IOException {
        Path inputPath = Paths.get(inputDirectory);
        Files.createDirectories(inputPath);

        int processedCount = 0;
        try (Stream<Path> paths = Files.list(inputPath)) {
            List<Path> files = paths
                    .filter(Files::isRegularFile)
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .toList();

            for (Path file : files) {
                String fileName = file.getFileName().toString();

                if (processedFileRepository.existsByFileName(fileName)) {
                    LOGGER.info("Skipping already processed file: {}", fileName);
                    continue;
                }

                processFile(file);
                processedFileRepository.save(new ProcessedFile(fileName, LocalDateTime.now()));
                processedCount++;
            }
        }

        return processedCount;
    }

    private void processFile(Path file) throws IOException {
        // Replace this with your business logic.
        long lineCount;
        try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)) {
            lineCount = lines.count();
        }
        LOGGER.info("Processed file: {} ({} lines)", file.getFileName(), lineCount);
    }
}

