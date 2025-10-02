package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.project.CommunityDto;
import com.example.funding.mapper.CommunityMapper;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.ReplyMapper;
import com.example.funding.mapper.UserMapper;
import com.example.funding.model.Community;
import com.example.funding.model.Reply;
import com.example.funding.model.User;
import com.example.funding.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityMapper communityMapper;
    private final UserMapper userMapper;
    private final ReplyMapper replyMapper;

    /**
     * <p>프로젝트 상세 페이지 내 커뮤니티&후기 목록 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @param code CM 또는 RV
     * @param pager pager
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-03
     */
    @Override
    public ResponseEntity<ResponseDto<PageResult<CommunityDto>>> getCommunity(Long projectId, String code, Pager pager) {
        int total = communityMapper.countTotal(projectId, code);

        List<Community> items = Collections.emptyList();
        if (total > 0) {
            items = communityMapper.getCommunityList(projectId, code, pager);
        }

        //Community에 Reply도 가져와서 CommunityDto로 매핑
        List<CommunityDto> list = items.stream()
                .map(cm -> {
                    User user = userMapper.getUserById(cm.getUserId());
                    List<Reply> replyList = replyMapper.getReplyListById(cm.getCmId(), code.trim());

                    return CommunityDto.builder()
                            .cmId(cm.getCmId())
                            .nickname(user.getNickname())
                            .profileImg(user.getProfileImg())
                            .cmContent(cm.getCmContent())
                            .rating(cm.getRating())
                            .createdAt(cm.getCreatedAt())
                            .code(cm.getCode())
                            .replyList(replyList)
                            .build();
                })
                .toList();
        PageResult<CommunityDto> result = PageResult.of(list, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "커뮤니티&후기 조회 성공", result));
    }
}
