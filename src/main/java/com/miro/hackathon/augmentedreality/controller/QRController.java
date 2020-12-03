package com.miro.hackathon.augmentedreality.controller;

import com.miro.hackathon.augmentedreality.service.QRService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class QRController {

    private final QRService qrService;

    @SneakyThrows
    @PostMapping("/qr-code")
    public ResponseEntity<Resource> generate(@NotBlank @RequestParam("url") String url) {
        final Resource cleanedImage = qrService.generate(url);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + UUID.randomUUID().toString())
                .body(cleanedImage);
    }
}
