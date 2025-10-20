package com.example.funding.dto.response.creator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReviewListDto {
    private Long cmId;
    private String cmContent;
    private LocalDateTime createdAt;
    private UserInfo user;
    private ProjectInfo project;
    private List<String> images;

    @Getter
    @Setter
    public static class UserInfo {
        private Long userId;
        private String nickname;
        private String profileImg;
    }

    @Getter
    @Setter
    public static class ProjectInfo {
        private Long projectId;
        private String title;
        private String thumbnail;
    }

    @Getter
    @Setter
    public static class Review {
        private Long cmId;
        private Long userId;
        private Long projectId;
        private String cmContent;
        private LocalDateTime createdAt;
    }

    @Getter
    @Setter
    public static class Image {
        private Long cmId;
        private String url;
    }
}
