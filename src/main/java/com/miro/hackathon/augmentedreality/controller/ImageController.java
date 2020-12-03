package com.miro.hackathon.augmentedreality.controller;

import com.miro.hackathon.augmentedreality.controller.response.UnprocessedResponse;
import com.miro.hackathon.augmentedreality.service.ImageService;
import com.miro.hackathon.augmentedreality.service.ImageProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final ImageProcessingService imageProcessingService;

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

    @PostMapping(value = "/cut", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> removeBackground(@RequestParam("image") MultipartFile originalFile) {

        final Resource cleanedImage = imageProcessingService.removeBackground(originalFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + generateName(originalFile))
                .body(cleanedImage);

    }

    private String generateName(MultipartFile originalFile) {
        return originalFile.getOriginalFilename() + "_cut.png";
    }
}
