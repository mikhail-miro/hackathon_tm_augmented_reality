package com.miro.hackathon.augmentedreality.service;

import com.miro.hackathon.augmentedreality.entity.ImageEntity;
import com.miro.hackathon.augmentedreality.repository.ImageEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageEntityRepository repository;

    public void saveImage(Long boardId, MultipartFile image) throws IOException {
        byte[] bytes = image.getBytes();
        String imageAsBase64 = Base64.getEncoder().encodeToString(bytes);

        repository.save(new ImageEntity(boardId, "SOME URL", Boolean.FALSE, LocalDateTime.now()));
    }

    public void markImageAsProcessed(Long imageId) {
        ImageEntity image = repository.findById(imageId).orElseThrow(() -> new RuntimeException("Image not found."));
        image.setProcessed(Boolean.TRUE);

        repository.save(image);
    }

    public List<ImageEntity> findAllUnprocessedImagesByBoard(Long boardId) {
        return repository.findAllByBoardIdAndProcessedFalse(boardId);
    }
}
