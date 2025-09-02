package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.user.LoginUserDto;
import com.example.funding.dto.response.user.MyPageBackingListDto;
import com.example.funding.dto.response.user.MyPageUserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(Long userId);

    ResponseEntity<ResponseDto<MyPageUserDto>> getMyPageUser(Long userId);

    ResponseEntity<ResponseDto<MyPageBackingListDto>> getMyPageBackingList(Long userId);
}
