package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.SearchProjectDto;
import com.example.funding.dto.response.project.FeaturedProjectDto;
import com.example.funding.dto.response.project.ProjectDetailDto;
import com.example.funding.dto.response.project.RecentTop10ProjectDto;
import com.example.funding.exception.FeaturedProjectNotFoundException;
import com.example.funding.exception.ProjectNotFoundException;
import com.example.funding.exception.RecentPaidProjectNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectService {

    ResponseEntity<ResponseDto<ProjectDetailDto>> getProjectDetail(Long projectId);

    /**
     * <p>최근 24시간 내 결제된 프로젝트 TOP10 조회</p>
     * <p>트렌드 점수 산정 기준 (가중치)</p>
     * <ul>
     *     <li>최근 24시간 결제금액/목표금액 비중: 70%</li>
     *     <li>좋아요 수: 20%</li>
     *     <li>조회수: 10%</li>
     * </ul>
     * <p>조건</p>
     * <ul>
     *     <li>프로젝트 상태: OPEN</li>
     *     <li>프로젝트 시작일: 최근 30일 이내</li>
     * </ul>
     *
     * @return 최근 24시간 내 결제된 프로젝트 TOP10 리스트
     * @throws RecentPaidProjectNotFoundException 최근 24시간 내 결제된 프로젝트가 없을 경우
     * @author by: 장민규
     * @since 2025-09-03
     */
    ResponseEntity<ResponseDto<List<RecentTop10ProjectDto>>> getRecentTop10();

    /**
     * <p>추천 프로젝트 조회</p>
     * <p>추천 알고리즘 점수 산정 기준 (가중치)</p>
     * <ul>
     *     <li>달성률: 50%</li>
     *     <li>후원자 수: 20%</li>
     *     <li>좋아요 수: 20%</li>
     *     <li>조회수: 10%</li>
     * </ul>
     * <p>조건</p>
     * <ul>
     *     <li>프로젝트 상태: OPEN</li>
     *     <li>프로젝트 시작일: 최근 N일 이내</li>
     * </ul>
     *
     * @param days  최근 N일 이내 시작한 프로젝트
     * @param limit 최대 조회 개수
     * @return 추천 프로젝트 리스트
     * @throws FeaturedProjectNotFoundException 추천 프로젝트가 없을 경우
     * @author by: 장민규
     * @since 2025-09-04
     */
    ResponseEntity<ResponseDto<List<FeaturedProjectDto>>> getFeatured(int days, int limit);

    ResponseEntity<ResponseDto<PageResult<FeaturedProjectDto>>> searchProject(SearchProjectDto dto, Pager pager);

    /**
     * <p>특정 프로젝트의 좋아요 수 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @return 프로젝트의 좋아요 수
     * @throws ProjectNotFoundException 프로젝트를 찾을 수 없을 경우
     * @author 장민규
     * @since 2025-10-15
     */
    ResponseEntity<ResponseDto<Long>> getLikeCnt(Long projectId);
}
