package com.example.batchfile.repository;

import com.example.batchfile.entity.ProcessedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedFileRepository extends JpaRepository<ProcessedFile, Long> {
    boolean existsByFileName(String fileName);
}

