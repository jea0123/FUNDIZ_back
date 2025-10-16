package com.example.funding.service.impl;

import com.example.funding.common.Cursor;
import com.example.funding.common.CursorPage;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.QnaAddRequestDto;
import com.example.funding.dto.response.project.QnaDto;
import com.example.funding.enums.NotificationType;
import com.example.funding.exception.notfound.CreatorNotFoundException;
import com.example.funding.exception.notfound.ProjectNotFoundException;
import com.example.funding.exception.notfound.UserNotFoundException;
import com.example.funding.handler.NotificationPublisher;
import com.example.funding.mapper.CreatorMapper;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.QnaMapper;
import com.example.funding.mapper.UserMapper;
import com.example.funding.model.Creator;
import com.example.funding.model.Project;
import com.example.funding.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class QnaServiceImpl implements QnaService {

    private final QnaMapper qnaMapper;
    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;
    private final CreatorMapper creatorMapper;

    private final NotificationPublisher notificationPublisher;

    /**
     * <p>QnA 내역 목록 조회(프로젝트 상세 페이지 기준)</p>
     *
     * @param projectId     프로젝트 ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId        마지막 항목의 qnaId
     * @param size          한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-07
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CursorPage<QnaDto>>> getQnaListOfProject(Long projectId, LocalDateTime lastCreatedAt, Long lastId, int size) {
        if (projectMapper.findById(projectId) == null) throw new ProjectNotFoundException();
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
     * @param userId    사용자 ID
     * @param qnaDto    QnaAddRequestDto
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-10-08
     */
    @Override
    public ResponseEntity<ResponseDto<String>> addQuestion(Long projectId, Long userId, QnaAddRequestDto qnaDto) {
        Project existingProject = projectMapper.findById(projectId);
        if (existingProject == null) throw new ProjectNotFoundException();

        if (userMapper.getUserById(userId) == null) throw new UserNotFoundException();

        Creator existingCreator = creatorMapper.findById(existingProject.getCreatorId());
        if (existingCreator == null) throw new CreatorNotFoundException();

        QnaDto item = QnaDto.builder()
                .projectId(projectId)
                .userId(userId)
                .content(qnaDto.getContent())
                .createdAt(qnaDto.getCreatedAt())
                .build();

        qnaMapper.addQuestion(item);
        notificationPublisher.publish(existingCreator.getUserId(), NotificationType.QNA_NEW, existingProject.getTitle(), projectId);
        return ResponseEntity.ok(ResponseDto.success(200, "문의 등록 성공", "데이터 출력확인"));
    }
}
