package com.miro.hackathon.augmentedreality.controller;

import com.miro.hackathon.augmentedreality.controller.response.UnprocessedResponse;
import com.miro.hackathon.augmentedreality.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/unprocessed")
    public UnprocessedResponse findUnprocessedImages(@RequestParam("boardId") String boardId) {
        return new UnprocessedResponse(imageService.findAllUnprocessedImagesByBoard(boardId));
    }


    @PostMapping("/save")
    public ResponseEntity saveImage(@RequestParam("image") MultipartFile image, @RequestParam("boardId") String boardId, @RequestParam("userId") String userId) {
        String imageUrl = imageService.saveImage(boardId, userId, image);
        return ResponseEntity.created(URI.create(imageUrl)).build();
    }

    @PutMapping("{imageId}/mark")
    public ResponseEntity markAsProcessed(@PathVariable Long imageId) {
        imageService.markImageAsProcessed(imageId);
        return ResponseEntity.ok().build();
    }
}
