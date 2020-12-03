package com.miro.hackathon.augmentedreality.service;

import com.miro.hackathon.augmentedreality.entity.ImageEntity;
import com.miro.hackathon.augmentedreality.repository.ImageEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageEntityRepository repository;
    private final CloudinaryService cloudinaryService;

    @SneakyThrows
    public String saveImage(String boardId, MultipartFile image) {
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

    public List<ImageEntity> findAllUnprocessedImagesByBoard(String boardId) {
        return repository.findAllByBoardIdAndProcessedFalse(boardId);
    }
}
