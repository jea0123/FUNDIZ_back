package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.notice.NoticeDto;
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

    @Override
    public ResponseEntity<ResponseDto<List<Notice>>> list() {
        List<Notice> noticeList =  noticeMapper.list();
        if (noticeList != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseDto.fail(409,"공지사항 목록 조회 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"공지사항 목록 조회 성공", noticeList));
    }

    @Override
    public ResponseEntity<ResponseDto<Notice>> noticeDetail(Long noticeId) {
        noticeMapper.updateViewCnt(noticeId);
        Notice noticeDetail = noticeMapper.noticeDetail(noticeId);
        if (noticeDetail != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseDto.fail(409,"공지사항 목록 조회 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"공지사항 목록 조회 성공", noticeDetail));
    }

    @Override
    public ResponseEntity<ResponseDto<Notice>> add(Notice item) {
        Notice noticeAdd = noticeMapper.add(item);
        if (noticeAdd != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseDto.fail(409,"공지사항 등록 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"공지사항 등록 성공", noticeAdd));
    }

    @Override
    public ResponseEntity<ResponseDto<Notice>> update(Notice item) {
        Notice noticeUpdate =  noticeMapper.update(item);
        if (noticeUpdate != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseDto.fail(409,"공지사항 수정 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"공지사항 수정 성공", noticeUpdate));
    }

    @Override
    public void delete(Long noticeId) {
        Notice noticeDelete = noticeMapper.delete(noticeId);
        if (noticeDelete != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseDto.fail(409,"공지사항 삭제 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"공지사항 삭제 성공", noticeDelete));
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
