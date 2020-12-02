package com.miro.hackathon.augmentedreality.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long boardId;
    @Column(length = 123123)
    private String imageAsBase64;
    private Boolean processed;
    private LocalDateTime createdAt;

    public ImageEntity(Long boardId, String imageAsBase64, Boolean processed, LocalDateTime createdAt) {
        this.boardId = boardId;
        this.imageAsBase64 = imageAsBase64;
        this.processed = processed;
        this.createdAt = createdAt;
    }
}
