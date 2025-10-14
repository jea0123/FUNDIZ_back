package com.example.funding.dto.request.creator;

import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectCreateRequestDto {
    private Long projectId;
    private Long subctgrId;
    private Long creatorId;

    //프로젝트
    private String title;
    private String content;
    private MultipartFile thumbnail;
    private String thumbnailUrl;
    private Integer goalAmount;
    private LocalDate startDate;
    private LocalDate endDate;

    //태그
    private List<String> tagList;

    //리워드
    private List<RewardCreateRequestDto> rewardList;

    //창작자
    private String creatorName;
    private String businessNum;
    private String email;
    private String phone;

    private List<MultipartFile> files;
    private MultipartFile businessDoc;
}
