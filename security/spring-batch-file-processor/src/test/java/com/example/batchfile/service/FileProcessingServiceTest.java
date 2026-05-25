package com.example.batchfile.service;

import com.example.batchfile.repository.ProcessedFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "app.scheduler.enabled=false",
        "spring.batch.job.enabled=false"
})
class FileProcessingServiceTest {

    @Autowired
    private FileProcessingService fileProcessingService;

    @Autowired
    private ProcessedFileRepository processedFileRepository;

    @TempDir
    Path tempInputDirectory;

    @BeforeEach
    void setUp() {
        processedFileRepository.deleteAll();
        ReflectionTestUtils.setField(fileProcessingService, "inputDirectory", tempInputDirectory.toString());
    }

    @Test
    void shouldProcessOnlyNewFiles() throws Exception {
        Path firstFile = tempInputDirectory.resolve("orders-1.csv");
        Files.writeString(firstFile, "id,amount\n1,25", StandardCharsets.UTF_8);

        int firstRunCount = fileProcessingService.processNewFiles();
        int secondRunCount = fileProcessingService.processNewFiles();

        Path secondFile = tempInputDirectory.resolve("orders-2.csv");
        Files.writeString(secondFile, "id,amount\n2,30", StandardCharsets.UTF_8);

        int thirdRunCount = fileProcessingService.processNewFiles();

        assertThat(firstRunCount).isEqualTo(1);
        assertThat(secondRunCount).isEqualTo(0);
        assertThat(thirdRunCount).isEqualTo(1);
        assertThat(processedFileRepository.count()).isEqualTo(2);
    }
}

