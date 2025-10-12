package com.example.funding.mapper;

import com.example.funding.common.Pager;
import com.example.funding.dto.response.project.QnaDto;
import com.example.funding.model.Qna;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface QnaMapper {

    int qnaTotalOfProject(Long projectId);

    List<QnaDto> getQnaListOfProject(@Param("projectId") Long projectId,
                                     @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
                                     @Param("lastId") Long lastId,
                                     @Param("size") int size);

    int addQuestion(QnaDto item);
}
