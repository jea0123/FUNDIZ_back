package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.mapper.NoticeMapper;
import com.example.funding.model.Notice;
import com.example.funding.service.CSService;
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
public class CSServiceImpl implements CSService {

    private final NoticeMapper noticeMapper;

    @Override
    public ResponseEntity<ResponseDto<List<Notice>>> noticeList() {
        List<Notice> noticeList =  noticeMapper.noticeList();
        if (noticeList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(409,"공지사항 목록 조회 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"공지사항 목록 조회 성공", noticeList));
    }

    @Override
    public ResponseEntity<ResponseDto<Notice>> item(Long noticeId) {
        noticeMapper.updateViewCnt(noticeId);

        Notice item =  noticeMapper.noticeDetail(noticeId);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(409,"공지사항 목록 조회 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"공지사항 목록 조회 성공", item));
    }

    @Override
    public void add(ResponseEntity<ResponseDto<Notice>> item) {
        noticeMapper.add(item);
    }

    /*
     *
     * <p>공지사항 게시판</p>
     * <p>조회수 +1</p>
     * @param noticeId
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @since 2025-08-31
     * @author by: 이동혁
     */


}
