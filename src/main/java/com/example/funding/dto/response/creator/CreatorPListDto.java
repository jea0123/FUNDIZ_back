package com.example.funding.dto.response.creator;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreatorPListDto {
    private Long creatorId;
    private Long projectId;
    private String title;
    private Long goalAmount;
    private Long currAmount;
    private String thumbnail;
    private String projectStatus;
    private Date createdAt;
}
