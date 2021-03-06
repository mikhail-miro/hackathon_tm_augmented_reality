package com.miro.hackathon.augmentedreality.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.miro.hackathon.augmentedreality.config.CloudinaryConfig;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private static final String KEY_FOR_URL = "secure_url";

    public CloudinaryService(CloudinaryConfig config) {
        this.cloudinary = new Cloudinary(config.getCloudinaryUrl());
    }

    @SneakyThrows
    public String uploadImageToCloudinary(MultipartFile image) {
        Map<String, String> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get(KEY_FOR_URL);
    }
}
