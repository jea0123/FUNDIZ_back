package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.user.BackingDetailDto;
import com.example.funding.dto.response.user.LoginUserDto;
import com.example.funding.dto.response.user.BackingListDto;
import com.example.funding.dto.response.user.MyPageUserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(Long userId);

    ResponseEntity<ResponseDto<MyPageUserDto>> getMyPageUser(Long userId);

    ResponseEntity<ResponseDto<List<BackingDetailDto>>> getBackingList(Long userId);

    ResponseEntity<ResponseDto<BackingDetailDto>> getBackingDetail(Long userId, Long projectId);
}
