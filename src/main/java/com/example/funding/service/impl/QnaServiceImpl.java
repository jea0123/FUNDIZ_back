package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.QnaAddRequestDto;
import com.example.funding.dto.response.project.Cursor;
import com.example.funding.dto.response.project.CursorPage;
import com.example.funding.dto.response.project.QnaDto;
import com.example.funding.mapper.QnaMapper;
import com.example.funding.mapper.ReplyMapper;
import com.example.funding.model.Qna;
import com.example.funding.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QnaServiceImpl implements QnaService {

    private final QnaMapper qnaMapper;
    private final ReplyMapper replyMapper;

    /**
     * <p>QnA 내역 목록 조회(프로젝트 상세 페이지 기준)</p>
     *
     * @param projectId 프로젝트 ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId 마지막 항목의 cmId
     * @param size 한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-07
     */
    @Override
    public ResponseEntity<ResponseDto<CursorPage<QnaDto>>> getQnaListOfProject(Long projectId, LocalDateTime lastCreatedAt, Long lastId, int size) {
        List<QnaDto> qnaList = qnaMapper.getQnaListOfProject(projectId, lastCreatedAt, lastId, size);

        Cursor next = null;
        if (!qnaList.isEmpty()) {
            QnaDto last = qnaList.getLast();
            next = new Cursor(last.getCreatedAt(), last.getQnaId());
        }

        return ResponseEntity.ok(ResponseDto.success(200, "QnA 목록 조회 성공", CursorPage.of(qnaList, next)));
    }

    /**
     * <p>QnA 질문 등록(프로젝트 상세 페이지 내)</p>
     *
     * @param projectId 프로젝트 ID
     * @param userId 사용자 ID
     * @param qnaDto QnaAddRequestDto
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-10-08
     */
    @Override
    public ResponseEntity<ResponseDto<String>> addQuestion(Long projectId, Long userId, QnaAddRequestDto qnaDto) {
        QnaDto item = QnaDto.builder()
                .projectId(projectId)
                .userId(userId)
                .content(qnaDto.getContent())
                .createdAt(qnaDto.getCreatedAt())
                .build();

        int result = qnaMapper.addQuestion(item);

        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "문의 등록 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "문의 등록 성공", "데이터 출력확인"));
    }
}
