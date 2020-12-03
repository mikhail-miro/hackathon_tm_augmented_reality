package com.miro.hackathon.augmentedreality.controller.response;

import com.miro.hackathon.augmentedreality.entity.ImageEntity;
import lombok.Value;

import java.util.List;

@Value
public class UnprocessedResponse {
    List<ImageEntity> items;
}
