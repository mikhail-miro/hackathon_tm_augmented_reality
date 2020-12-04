package com.miro.hackathon.augmentedreality.service;

import com.miro.hackathon.augmentedreality.config.BASNetConfig;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.miro.hackathon.augmentedreality.utility.ImageUtils.createResizedCopy;

@Slf4j
@Service
@RequiredArgsConstructor
public class BASNetService {

    private static final String BACKEND = "basNet";
    private final BASNetConfig basNetConfig;

    @CircuitBreaker(name = BACKEND, fallbackMethod = "fallback")
    @Bulkhead(name = BACKEND, fallbackMethod = "fallback")
    public Optional<BufferedImage> getMask(byte[] file) {
        try {
            byte[] mask = getMaskFromBasNet(file);
            return Optional.of(scaleMask(new ByteArrayInputStream(file), new ByteArrayInputStream(mask)));
        } catch (IOException e) {
            log.error("Cannot get mask for image from BASNet.", e);
        }
        return Optional.empty();
    }

    public Optional<BufferedImage> fallback(byte[] file, Exception ex) {
        log.warn("Fallback method was called.");
        return Optional.empty();
    }

    private byte[] getMaskFromBasNet(byte[] file) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(basNetConfig.getHostname());
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("data", file, ContentType.DEFAULT_BINARY, "data");
        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(uploadFile);
        return response.getEntity().getContent().readAllBytes();
    }

    @SneakyThrows
    private BufferedImage scaleMask(InputStream original, InputStream mask) {
        BufferedImage originalImage = ImageIO.read(original);
        BufferedImage maskImage = ImageIO.read(mask);

        return createResizedCopy(maskImage, originalImage.getWidth(), originalImage.getHeight(), true);
    }

}
