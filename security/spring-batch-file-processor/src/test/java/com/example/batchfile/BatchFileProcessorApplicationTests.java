package com.example.batchfile;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "app.scheduler.enabled=false",
        "spring.batch.job.enabled=false"
})
class BatchFileProcessorApplicationTests {

    @Test
    void contextLoads() {
    }
}

