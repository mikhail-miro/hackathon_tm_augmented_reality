package com.miro.hackathon.augmentedreality.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.miro.hackathon.augmentedreality.utility.ImageUtils.applyTransparency;
import static com.miro.hackathon.augmentedreality.utility.ImageUtils.transformGrayToTransparency;

@Slf4j
@Service
public class ImageProcessingService {

    private final BASNetService basNetService;

    public ImageProcessingService(BASNetService basNetService) {
        this.basNetService = basNetService;
    }

    @SneakyThrows
    public Resource removeBackground(MultipartFile originalFile) {
        log.info("Got file: {} for cutting with size: {}", originalFile.getOriginalFilename(), originalFile.getSize());
        final byte[] fileContent = originalFile.getInputStream().readAllBytes();

        final BufferedImage scaledMask = basNetService.getMask(fileContent);
        final BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(fileContent));

        BufferedImage cleanedImage = applyTransparency(originalImage, transformGrayToTransparency(scaledMask));

        return convertToResource(cleanedImage);
    }

    private InputStreamResource convertToResource(BufferedImage scaledMask) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(scaledMask, "png", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return new InputStreamResource(is);
    }


}
