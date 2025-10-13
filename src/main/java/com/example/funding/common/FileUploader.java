package com.example.funding.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Getter
@Setter
public class FileUploader {
    private final String filePath = "D:/upload";

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
