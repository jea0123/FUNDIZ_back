package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.project.CommunityDto;
import com.example.funding.dto.response.project.ProjectDetailDto;
import com.example.funding.dto.response.project.SubcategoryDto;
import com.example.funding.mapper.*;
import com.example.funding.model.*;
import com.example.funding.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectMapper projectMapper;
    private final SubcategoryMapper subcategoryMapper;
    private final TagMapper tagMapper;
    private final RewardMapper rewardMapper;
    private final NewsMapper newsMapper;
//    private final CommunityMapper communityMapper;
//    private final UserMapper userMapper;
//    private final ReplyMapper replyMapper;

    /**
     * <p>프로젝트 상세 페이지 조회</p>
     * <p>조회수 +1</p>
     * @param projectId
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 조은애
     * @since 2025-08-31
     */
    @Override
    @Transactional
    public ResponseEntity<ResponseDto<ProjectDetailDto>> getProjectDetail(Long projectId) {
        //조회수 증가
        projectMapper.updateViewCnt(projectId);

        Project project = projectMapper.getProjectById(projectId);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "프로젝트를 찾을 수 없습니다."));
        }

        SubcategoryDto subcategory = subcategoryMapper.getSubcategoryById(project.getSubctgrId());

        List<Tag> tagList = tagMapper.getTagListById(projectId);
        List<Reward> rewardList = rewardMapper.getRewardListById(projectId);
        List<News> newsList = newsMapper.getNewsListById(projectId);
/*
        List<Community> communityList = communityMapper.getCommunityListById(projectId);
        //Community에 Reply도 가져온 다음에(map) CommunityDto로 변환
        List<CommunityDto> communityDtoList = communityList.stream()
                .map(cm -> {
                    User user = userMapper.getUserById(cm.getUserId());
                    List<Reply> replyList = replyMapper.getReplyListById(cm.getCmId());

                    return CommunityDto.builder()
                        .cmId(cm.getCmId())
                        .nickname(user.getNickname())
                        .profileImg(user.getProfileImg())
                        .content(cm.getContent())
                        .rating(cm.getRating())
                        .createdAt(cm.getCreatedAt())
                        .code(cm.getCode())
                        .replyList(replyList)
                        .build();
                })
                .toList();
*/

        ProjectDetailDto projectDetailDto = ProjectDetailDto.builder()
                .projectId(project.getProjectId())
                .title(project.getTitle())
                .goalAmount(project.getGoalAmount())
                .currAmount(project.getCurrAmount())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .content(project.getContent())
                .thumbnail(project.getThumbnail())
                .projectStatus(project.getProjectStatus())
                .backerCnt(project.getBackerCnt())
                .viewCnt(project.getViewCnt())
                .creatorId(project.getCreatorId())
                .subcategory(subcategory)
                .tagList(tagList)
                .rewardList(rewardList)
                .newsList(newsList)
//                .communityList(communityDtoList)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "프로젝트 상세 조회 성공", projectDetailDto));
    }
}
