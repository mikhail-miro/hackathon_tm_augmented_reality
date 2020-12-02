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
    private final CloudinaryService cloudinaryService;

    public String saveImage(Long boardId, MultipartFile image) throws IOException {
        String imageUrl = cloudinaryService.uploadImageToCloudinary(image);
        ImageEntity entity = new ImageEntity(boardId, imageUrl, Boolean.FALSE, LocalDateTime.now());
        repository.save(entity);

        return imageUrl;
    }

    public void markImageAsProcessed(Long imageId) {
        ImageEntity entity = repository.findById(imageId).orElseThrow(() -> new RuntimeException("Image not found."));
        entity.setProcessed(Boolean.TRUE);

        repository.save(entity);
    }

    public List<ImageEntity> findAllUnprocessedImagesByBoard(Long boardId) {
        return repository.findAllByBoardIdAndProcessedFalse(boardId);
    }
}
