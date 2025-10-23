package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Attach {
    private Long attachId;
    private Long inqId;
    private Long reportId;
    private Long noticeId;
    private Long cmId;
    private String fileName;
    private String filePath;
    private String fileType;
    private String fileExt;
    private String code;
}
