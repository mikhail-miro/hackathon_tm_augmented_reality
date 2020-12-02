package com.miro.hackathon.augmentedreality.controller;

import com.miro.hackathon.augmentedreality.entity.ImageEntity;
import com.miro.hackathon.augmentedreality.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/unprocessed")
    public List<ImageEntity> findUnprocessedImages(@RequestParam("boardId") Long boardId) {
        return imageService.findAllUnprocessedImagesByBoard(boardId);
    }

    @PostMapping("/save")
    public ResponseEntity saveImage(@RequestParam("image") MultipartFile image, @RequestParam("boardId") Long boardId) throws IOException {
        imageService.saveImage(boardId, image);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{imageId}/mark")
    public ResponseEntity markAsProcessed(@PathVariable Long imageId) {
        imageService.markImageAsProcessed(imageId);
        return ResponseEntity.ok().build();
    }
}
