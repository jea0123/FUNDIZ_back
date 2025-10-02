package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.creator.CreatorPListDto;
import com.example.funding.mapper.CreatorMapper;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.model.Creator;
import com.example.funding.service.CreatorService;
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
public class CreatorServiceImpl implements CreatorService {
    private final CreatorMapper creatorMapper;
    private final ProjectMapper projectMapper;

    @Override
    public ResponseEntity<ResponseDto<List<CreatorPListDto>>> getCreatorPList(Long creatorId) {
        Creator creator =creatorMapper.findById(creatorId);

        if(creator==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "크리에이터의 프로젝트 목록을 찾을 수 없습니다."));
        }
        List<CreatorPListDto> creatorpList = projectMapper.getCreatorPList(creatorId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "크리에이터의 프로젝트 리스트 불러오기 성공", creatorpList));
    }
}
