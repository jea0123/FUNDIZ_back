package com.example.funding.dto.response.backing;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BackingCreatorProjectListDto {
    //프로젝트
    private Long creatorId;
    private Long projectId;
    private String title;
    private Long goalAmount;
    private Long currAmount;
    private String thumbnail;
    private Long backerCnt;

    List<BackingCreatorBackerList> backerList;

    //달성률
    private double completionRate;
}
