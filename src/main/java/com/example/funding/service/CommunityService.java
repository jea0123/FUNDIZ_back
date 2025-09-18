package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.project.CommunityDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommunityService {

    ResponseEntity<ResponseDto<PageResult<CommunityDto>>> getCommunity(Long projectId, String code, Pager pager);
}
