package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Files {
    private long fileId;
    private String fileName;
    private String filePath;
    private String fileSize;
    private String fileType;
    private String fileExt;
    private char code;
}
