package com.miro.hackathon.augmentedreality.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String boardId;
    private String imageUrl;
    private Boolean processed;
    private LocalDateTime createdAt;

    public ImageEntity(String boardId, String imageUrl, Boolean processed, LocalDateTime createdAt) {
        this.boardId = boardId;
        this.imageUrl = imageUrl;
        this.processed = processed;
        this.createdAt = createdAt;
    }
}
