package com.example.funding.service.impl;

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

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityMapper communityMapper;
    private final UserMapper userMapper;
    private final ReplyMapper replyMapper;

    @Override
    public ResponseEntity<ResponseDto<List<CommunityDto>>> getCommunity(Long projectId, String code, Pager pager) {
        //페이지네이션
        int total = communityMapper.countTotal(projectId, code);
        pager.setTotalElements(total);
        pager.setDefault();

        List<Community> communityList = communityMapper.getCommunityById(projectId, code, pager);

        if (communityList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "커뮤니티&후기를 찾을 수 없습니다."));
        }

        //Community에 Reply도 가져와서 CommunityDto로 매핑
        List<CommunityDto> communityDtoList = communityList.stream()
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

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "커뮤니티&후기 조회 성공", communityDtoList));
    }
}
