package com.miro.hackathon.augmentedreality.service;

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

import java.io.InputStream;
import java.net.http.HttpClient;

@Slf4j
@Service
public class BASNetService {

    private final String BASNetHost = "http://u2net-predictor.tenant-compass.global.coreweave.com/";
    //curl -v -X POST -F 'data=@/Users/mikhail/Downloads/IMG_6781.jpg' 'http://u2net-predictor.tenant-compass.global.coreweave.com/' -o mask.png

    private final HttpClient client = HttpClient.newBuilder().build();

    @SneakyThrows
    public InputStream getMask(byte[] file) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(BASNetHost);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("data", file, ContentType.DEFAULT_BINARY, "data");

        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(uploadFile);
        return response.getEntity().getContent();
    }

}
