package com.example.funding.dto.response.project;

import com.example.funding.model.Reply;

import java.util.Date;
import java.util.List;

public class CommunityDto {
    private Long cmId;
    private String nickname;
    private String profileImg;
    private String content;
    private int rating;
    private Date createdAt;
    private char code;

    private List<Reply> replyList;
}
