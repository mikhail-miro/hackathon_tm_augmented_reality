package com.miro.hackathon.augmentedreality.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CloudinaryConfig {

    @Value("${cloudinary.url}")
    private String cloudinaryUrl;
}
