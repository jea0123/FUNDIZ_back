package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.notice.NoticeDto;
import com.example.funding.dto.response.user.LoginUserDto;
import com.example.funding.mapper.NoticeMapper;
import com.example.funding.model.Notice;
import com.example.funding.service.NoticeService;
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
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    /*
     *
     * <p>공지사항 게시판</p>
     * <p>조회수 +1</p>
     * @param noticeId
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @since 2025-08-31
     * @author by: 이동혁
     */

    @Override
    @Transactional
    public ResponseEntity<ResponseDto<NoticeDto>> getNoticeList(Long noticeId) {
        noticeMapper.updateViewCnt(noticeId);
        Notice notice = noticeMapper.getNoticeById(noticeId);
        if (notice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "프로젝트를 찾을 수 없습니다."));
        }

        NoticeDto noticeDto = NoticeDto.builder()
                .noticeId(notice.getNoticeId())
                .adId(notice.getAdId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .viewCnt(notice.getViewCnt())
                .createdAt(notice.getCreatedAt())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "프로젝트 상세 조회 성공", notice));
    }

    @Override
    public ResponseEntity<ResponseDto<Notice>> add(Notice item) {
        return noticeMapper.add(item);
    }

    @Override
    public ResponseEntity<ResponseDto<Notice>> update(Notice item) {
        return noticeMapper.update(item);
    }

    @Override
    public void delete(Long noticeId) {
        noticeMapper.delete(noticeId);
    }
}
