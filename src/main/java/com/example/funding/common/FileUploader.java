package com.example.funding.common;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploader {
//    private final Path baseDir = Paths.get("D:/upload");
//    private final String publicPrefix = "/static/uploads";
//
//    public String upload(MultipartFile file) throws IOException {
//        if (file == null || file.isEmpty()) return null;
//
//        Files.createDirectories(baseDir);
//
//        String originalFilename = file.getOriginalFilename();
//        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        String uuid = UUID.randomUUID().toString();
//
//        String saveFilename = uuid + extension;
//
//        Path savePath = baseDir.resolve(saveFilename);
//        file.transferTo(savePath.toFile());
//
//        String url = ServletUriComponentsBuilder
//            .fromCurrentContextPath()
//            .path(publicPrefix + "/")
//            .path(saveFilename)
//            .toUriString();
//
//        return url;
//    }

    private final String filePath = "D:/upload/";

    public String upload(MultipartFile file) throws IOException {
        if (file.isEmpty()) return null;

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String saveFilename = uuid + extension;
        String savePath = filePath + saveFilename;

        file.transferTo(new File(savePath));

        return savePath;
    }
}
