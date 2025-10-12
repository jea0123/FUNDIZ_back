package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.user.*;
import com.example.funding.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    /**
     * <p>로그인 사용자 정보 조회</p>
     *
     * @param userId 인증된 사용자의 ID
     * @return 로그인 사용자 정보
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-08-28
     */
    ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(Long userId);

    ResponseEntity<ResponseDto<MyPageUserDto>> getMyPageUser(Long userId);

    ResponseEntity<ResponseDto<List<MyPageLikedDto>>> getLikedList(Long userId);

    ResponseEntity<ResponseDto<List<MyPageQnADto>>> getQnAList(Long userId);

    /**
     * <p>최근 본 프로젝트 목록 조회</p>
     *
     * @param userId 인증된 사용자의 ID
     * @return 최근 본 프로젝트 목록
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-09-05
     */
    ResponseEntity<ResponseDto<List<RecentViewProject>>> getRecentViewProjects(Long userId);

    //서비스에서구현
    ResponseEntity<ResponseDto<MyPageQnADetailDto>> getQnADetail(Long userId, Long projectId);

    ResponseEntity<ResponseDto<String>> userNickname(Long userId);

    ResponseEntity<ResponseDto<String>> userProfileImg(Long userId);

    ResponseEntity<ResponseDto<String>> userpassword(Long userId);
}