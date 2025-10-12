package com.example.funding.mapper;

import com.example.funding.common.Pager;
import com.example.funding.dto.response.project.CommunityDto;
import com.example.funding.dto.response.project.ReviewDto;
import com.example.funding.model.Community;
import com.example.funding.model.Qna;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CommunityMapper {
    List<CommunityDto> getCommunityList(@Param("projectId") Long projectId,
                                       @Param("code") String code,
                                       @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
                                       @Param("lastId") Long lastId,
                                       @Param("size") int size);

    List<ReviewDto> getReviewList(@Param("projectId") Long projectId,
                                  @Param("code") String code,
                                  @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
                                  @Param("lastId") Long lastId,
                                  @Param("size") int size);

    int qnaTotalOfPJ(Long projectId);

    List<Qna> getQnaListOfPJ(Long projectId, @Param("pager") Pager pager);

    int addQuestion(Qna item);

    int createCommunity(Community community);
}
