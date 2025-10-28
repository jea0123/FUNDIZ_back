package com.example.funding.mapper;

import com.example.funding.dto.response.cs.InquiryReplyDto;
import com.example.funding.dto.response.cs.QnaReplyDto;
import com.example.funding.dto.response.project.ReplyDto;
import com.example.funding.model.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReplyMapper {
    List<ReplyDto> getReplyList(@Param("cmId") Long cmId,
                                @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
                                @Param("lastId") Long lastId,
                                @Param("size") int limitPlusOne);

    void createCommunityReply(Reply reply);

    List<InquiryReplyDto> getInquiryReplyList(@Param("inqId") Long inqId,
                                              @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
                                              @Param("lastId") Long lastId,
                                              @Param("size") int limitPlusOne);

    List<QnaReplyDto> getQnaReplyList(@Param("qnaId") Long qnaId,
                                      @Param("lastCreatedAt") LocalDateTime lastCreatedAt,
                                      @Param("lastId") Long lastId,
                                      @Param("size") int limitPlusOne);

    void createInquiryReply(Reply reply);

    void createQnaReply(Reply reply);

    void deleteReply(@Param("replyId") Long replyId);
}


