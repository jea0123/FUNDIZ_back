package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.user.*;
import com.example.funding.dto.response.user.LoginUserDto;
import com.example.funding.dto.response.user.MyPageUserDto;
import com.example.funding.mapper.BackingMapper;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.UserMapper;
import com.example.funding.model.Project;
import com.example.funding.model.User;
import com.example.funding.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    //대문자로 고치기
    private final BackingMapper backingMapper;

    /**
     * <p>로그인 사용자 정보 조회</p>
     * @param userId 인증된 사용자의 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @since 2025-08-28
     * @author by: 장민규
     */
    @Override
    public ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "사용자를 찾을 수 없습니다."));
        }
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .joinedAt(user.getJoinedAt())
                .followCnt(user.getFollowCnt())
                .isCreator(user.getIsCreator())
                .role(user.getRole().toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "사용자 정보 조회 성공", loginUserDto));
    }

    @Override
    public ResponseEntity<ResponseDto<MyPageUserDto>> getMyPageUser(Long userId) {
        User user = userMapper.getUserById(userId);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "유저 정보를 불러올 수 없습니다."));
        }
        MyPageUserDto mypageUserDto = MyPageUserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "유저 정보 불러오기 성공", mypageUserDto));
    }

    @Override
    public ResponseEntity<ResponseDto<List<BackingDetailDto>>> getBackingList(Long userId) {

        User user = userMapper.getUserById(userId);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404,"후원한 프로젝트 목록을 찾을 수 없습니다."));
        }
/*
        List<BackingDetailDto> backingList = backingMapper.get*/

        /*Project project = projectMapper.getProjectById(project)
        BackingProjectDto myPageBackingProject = backingMapper.getMyPageBackingProjectId*/
        /*
        Project project = projectMapper.getProjectById(userId);
        Project
        if(project==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "프로젝트 후원 목록을 찾을 수 없습니다"));
        }

        MyPageBackingProjectDto myPageBackingProject = backingMapper.getMyPageBackingProjectId(project.getProjectId());

        MyPageBackingListDto myPageBackingListDto = MyPageBackingListDto.builder()
                .backingId()
                .backingStatus()
                .rewardId()
                .build();
        */
        return null;
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDto<BackingDetailDto>> getBackingDetail(Long userId, Long projectId){
        User user = userMapper.getUserById(userId);
        Project project =projectMapper.getProjectById(projectId);

        if(user == null && project == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404,"후원한 해당 프로젝트를 찾을 수 없습니다."));
        }
        return null;
    }

}
