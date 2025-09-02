package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.project.ProjectDetailDto;
import org.springframework.http.ResponseEntity;

public interface ProjectService {

    ResponseEntity<ResponseDto<ProjectDetailDto>> getProjectDetail(Long projectId);


}
