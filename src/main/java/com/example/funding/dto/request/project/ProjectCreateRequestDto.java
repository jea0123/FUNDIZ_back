package com.example.funding.dto.request.project;

import com.example.funding.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProjectCreateRequestDto {
    //프로젝트
    private Long projectId;
    private Long subctgrId;
    private Long creatorId;
    private String title;
    private String content;
    private String thumbnail;
    private Integer goalAmount;
    private Date startDate;
    private Date endDate;

    //태그
    private List<String> tagList;
}
