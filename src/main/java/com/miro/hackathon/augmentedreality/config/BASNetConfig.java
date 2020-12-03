package com.miro.hackathon.augmentedreality.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class BASNetConfig {

    @Value("${basnet.hostname}")
    private String hostname;
}
