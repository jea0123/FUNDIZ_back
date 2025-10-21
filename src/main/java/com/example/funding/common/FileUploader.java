package com.example.funding.common;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class FileUploader {

    private final String namespace = "ax7olwlcbbtt";
    private final String bucketName = "funding-files";
    private final String bucketParId = "r17KOb5a_NsCD6LbWP4sakcP3CIoMHp5Y_iU2jeJpfVhfBSpbYxhZpnzMFVat8jL";
    private final HttpClient http = HttpClient.newHttpClient();

    @SneakyThrows
    public String upload(MultipartFile file) throws Exception {
        if (file.isEmpty()) return null;

        String original = file.getOriginalFilename();
        String ext = (original != null && original.lastIndexOf('.') > -1)
                ? original.substring(original.lastIndexOf('.')) : "";
        String objectName = UUID.randomUUID() + ext;
        String encoded = URLEncoder.encode(objectName, StandardCharsets.UTF_8);
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        String regionId = "ap-chuncheon-1";
        String uploadUrl = "https://objectstorage." + regionId
                + ".oraclecloud.com/p/" + bucketParId
                + "/n/" + namespace + "/b/" + bucketName + "/o/" + encoded;

        HttpRequest req = HttpRequest.newBuilder(URI.create(uploadUrl))
                .header("Content-Type", contentType)
                .PUT(HttpRequest.BodyPublishers.ofByteArray(file.getBytes()))
                .build();

        HttpResponse<Void> res = http.send(req, HttpResponse.BodyHandlers.discarding());
        if (res.statusCode() / 100 != 2) throw new IllegalStateException("Upload failed: " + res.statusCode());

        return "https://objectstorage." + regionId
                + ".oraclecloud.com/n/" + namespace + "/b/" + bucketName + "/o/" + encoded;
    }
}
