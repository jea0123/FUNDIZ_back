package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.mapper.NoticeMapper;
import com.example.funding.model.Notice;
import com.example.funding.service.NoticeService;
import com.example.funding.validator.Loaders;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoticeServiceImpl implements NoticeService {
    private final Loaders loaders;
    private final NoticeMapper noticeMapper;

    /**
     * <p>공지사항 목록 조회</p>
     *
     * @param pager Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-19
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<Notice>>> noticeList(Pager pager) {
        int total = noticeMapper.noticeTotal();

        List<Notice> noticeList = noticeMapper.noticeList(pager);

        PageResult<Notice> result = PageResult.of(noticeList, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "공지사항 목록 조회 성공", result));
    }

    /**
     * <p>공지사항 상세 페이지 조회</p>
     *
     * @param noticeId 공지사항 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-09-19
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<Notice>> item(Long noticeId) {
        Notice item = loaders.notice(noticeId);
        //조회수 증가
        noticeMapper.updateViewCnt(noticeId);
        //공지사항 상세페이지 조회
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "공지사항 상세 조회 성공", item));
    }
}



