package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.user.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(Long userId);

    ResponseEntity<ResponseDto<MyPageUserDto>> getMyPageUser(Long userId);

    ResponseEntity<ResponseDto<List<BackingDto>>> getBackingList(Long userId);

    ResponseEntity<ResponseDto<BackingDetailDto>> getBackingDetail(Long userId, Long projectId);


    ResponseEntity<ResponseDto<List<MyPageLikedDto>>> getLikedList(Long userId);

    ResponseEntity<ResponseDto<List<MyPageQnADto>>> getQnAList(Long userId);
    ResponseEntity<ResponseDto<List<RecentViewProject>>> getRecentViewProjects(Long userId);
}
