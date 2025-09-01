package com.example.funding.dto.response.user;

import com.example.funding.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MyPageUserDto {
    // 유저 테이블에서 가져올 정보
    private Long userId;
    private String email;
    private String nickname;
    private String profileImg;

    //창작자 테이블에서 가져올 정보
    private String creatorId;
    private String creatorName;
    private String creatorProfileImg;

    //후원한 프로젝트 목록
    private long backingId;
        // ENUM 타입으로 전환 필요
    private String backingStatus;
    private String rewardId;


    /*public class UserDTO{
        private Long userId;
        private String email;
        private String nickname;
        private String profileImg;
    }

    public class CreaterDTO{
        private String creatorId;
        private String creatorName;
        private String profileImg;
    }

    public class MyPageDTO{
        private UserDTO userDTo;
        private CreaterDTO creatorDTO;
    }*/
}
