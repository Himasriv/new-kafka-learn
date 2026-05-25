package com.example.batchfile.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_file", uniqueConstraints = {
        @UniqueConstraint(name = "uk_processed_file_name", columnNames = "file_name")
})
public class ProcessedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt;

    public ProcessedFile() {
    }

    public ProcessedFile(String fileName, LocalDateTime processedAt) {
        this.fileName = fileName;
        this.processedAt = processedAt;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
}

