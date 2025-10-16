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

    //프로젝트
    private String title;
    private Integer goalAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String content;
    private String contentBlocks;
    private String thumbnailUrl;
    private String businessDocUrl;

    //태그/리워드
    private List<String> tagList;
    private List<RewardCreateRequestDto> rewardList;

    //파일
    private MultipartFile thumbnail;
    private MultipartFile businessDoc;
}
