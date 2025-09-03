package com.example.funding.dto.response.project;

import com.example.funding.model.Reply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommunityDto {
    private Long cmId;
    private String nickname;
    private String profileImg;
    private String content;
    private Integer rating;
    private Date createdAt;
    private String code;

    private List<Reply> replyList;
}
