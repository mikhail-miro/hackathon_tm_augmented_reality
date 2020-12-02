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

import static com.miro.hackathon.augmentedreality.utility.ImageUtils.*;

@Slf4j
@Service
public class ProcessorService {

    private final BASNetService basNetService;

    public ProcessorService(BASNetService basNetService) {
        this.basNetService = basNetService;
    }

    @SneakyThrows
    public Resource removeBackground(MultipartFile originalFile) {
        log.info("Got file: {} size: {}", originalFile.getOriginalFilename(), originalFile.getSize());
        final byte[] fileContent = originalFile.getInputStream().readAllBytes();
        final byte[] mask = basNetService.getMask(fileContent).readAllBytes();

        final BufferedImage scaledMask = scaleMask(new ByteArrayInputStream(fileContent), new ByteArrayInputStream(mask));
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

    @SneakyThrows
    private BufferedImage scaleMask(InputStream original, InputStream mask) {
        BufferedImage originalImage = ImageIO.read(original);
        BufferedImage maskImage = ImageIO.read(mask);

        return createResizedCopy(maskImage, originalImage.getWidth(), originalImage.getHeight(), true);
    }


}
