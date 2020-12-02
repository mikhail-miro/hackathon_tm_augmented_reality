package com.miro.hackathon.augmentedreality.controller;

import com.miro.hackathon.augmentedreality.service.ProcessorService;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageCuttingController {

    private final ProcessorService processorService;

    public ImageCuttingController(ProcessorService processorService) {
        this.processorService = processorService;
    }

    @SneakyThrows
    @PostMapping("/api/image/cut")
    public ResponseEntity<Resource> removeBackground(@RequestParam("data") MultipartFile originalFile) {

        final Resource cleanedImage = processorService.removeBackground(originalFile);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + generateName(originalFile))
                .body(cleanedImage);

    }

    private String generateName(MultipartFile originalFile) {
        return originalFile.getOriginalFilename() + "_cleaned.png";
    }
}
