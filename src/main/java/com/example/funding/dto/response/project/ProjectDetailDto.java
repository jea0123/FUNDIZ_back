package com.example.funding.dto.response.project;

import com.example.funding.model.News;
import com.example.funding.model.Reward;
import com.example.funding.model.Tag;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProjectDetailDto {
    private Long projectId;
    private Long creatorId;
    private Long subctgrId;

    //프로젝트
    private String title;
    private Integer goalAmount;
    private Integer currAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String content;
    private String thumbnail;
    private String projectStatus;
    private Integer backerCnt;
    private Integer likeCnt;
    private Integer viewCnt;

    //계산 필드
    private Integer percentNow;
    private Integer projectCnt;
    private LocalDate paymentDate;

    //창작자
    private String creatorName;
    private Long followerCnt;
    private String profileImg;

    //카테고리
    private String ctgrName;
    private String subctgrName;
    //태그
    private List<Tag> tagList;
    //리워드
    private List<Reward> rewardList;
    //새소식
    private List<News> newsList;

    //프론트
    private Boolean isBackedByMe; // 내가 이 프로젝트 후원했는지
    private Boolean canWriteReview; // 내가 후기 작성할 수 있는지
}
