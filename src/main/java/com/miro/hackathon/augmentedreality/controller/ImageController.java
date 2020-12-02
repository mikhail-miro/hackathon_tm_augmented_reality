package com.miro.hackathon.augmentedreality.controller;

import com.miro.hackathon.augmentedreality.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/save")
    public ResponseEntity saveImage(@RequestParam("image") MultipartFile image, @RequestParam("boardId") Long boardId) throws IOException {
        imageService.saveImage(boardId, image);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/mark-as-processed")
    public ResponseEntity markAsProcessed(@RequestParam("imageId") Long imageId) {
        imageService.markImageAsProcessed(imageId);
        return ResponseEntity.ok().build();
    }
}
