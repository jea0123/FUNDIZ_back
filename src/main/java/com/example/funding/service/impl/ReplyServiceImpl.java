package com.example.funding.service.impl;

import com.example.funding.common.Cursor;
import com.example.funding.common.CursorPage;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.QnaReplyCreateRequestDto;
import com.example.funding.dto.request.cs.IqrReplyCreateRequestDto;
import com.example.funding.dto.request.project.ReplyCreateRequestDto;
import com.example.funding.dto.response.cs.InquiryReplyDto;
import com.example.funding.dto.response.cs.QnaReplyDto;
import com.example.funding.dto.response.project.ReplyDto;
import com.example.funding.enums.NotificationType;
import com.example.funding.exception.badrequest.ContentRequiredException;
import com.example.funding.handler.NotificationPublisher;
import com.example.funding.mapper.ReplyMapper;
import com.example.funding.model.Community;
import com.example.funding.model.Inquiry;
import com.example.funding.model.Qna;
import com.example.funding.model.Reply;
import com.example.funding.service.ReplyService;
import com.example.funding.validator.Loaders;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.funding.validator.Preconditions.requireHasText;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyServiceImpl implements ReplyService {

    private final Loaders loaders;
    private final ReplyMapper replyMapper;
    private final NotificationPublisher notificationPublisher;

    /**
     * <p>프로젝트 상세 페이지 - 커뮤니티 댓글 목록 조회</p>
     *
     * @param cmId          커뮤니티 ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId        마지막 항목의 id
     * @param size          한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-12
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CursorPage<ReplyDto>>> getReplyList(Long cmId, LocalDateTime lastCreatedAt, Long lastId, int size) {
        loaders.community(cmId);

        int limitPlusOne = Math.max(1, size) + 1;
        List<ReplyDto> replyList = replyMapper.getReplyList(cmId, lastCreatedAt, lastId, limitPlusOne);

        boolean hasMore = replyList.size() > size;
        if (hasMore) {
            replyList = replyList.subList(0, size);
        }

        Cursor next = null;
        if (hasMore && !replyList.isEmpty()) {
            ReplyDto last = replyList.getLast();
            next = new Cursor(last.getCreatedAt(), last.getCmId());
        }

        return ResponseEntity.ok(ResponseDto.success(200, "댓글 목록 조회 성공", CursorPage.of(replyList, next)));
    }

    /**
     * <p>프로젝트 상세 페이지 - 커뮤니티 댓글 등록</p>
     *
     * @param cmId   커뮤니티 ID
     * @param dto    ReplyCreateRequestDto
     * @param userId 사용자 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-13
     */
    @Override
    public ResponseEntity<ResponseDto<ReplyDto>> createCommunityReply(Long cmId, ReplyCreateRequestDto dto, Long userId) {
        loaders.user(userId);
        Community existingCommunity = loaders.community(cmId);

        String content = (dto.getContent() == null ? "" : dto.getContent().trim());
        requireHasText(content, ContentRequiredException::new);
        //TODO: 문자 길이 체크

        Reply reply = Reply.builder()
                .cmId(cmId)
                .userId(userId)
                .content(content)
                .isSecret(dto.getIsSecret())
                .code("CM")
                .build();

        replyMapper.createCommunityReply(reply);
        notificationPublisher.publish(existingCommunity.getUserId(), NotificationType.COMMUNITY_REPLY, existingCommunity.getCmContent(), cmId);
        return ResponseEntity.ok(ResponseDto.success(200, "댓글 등록 성공", null));
    }

    /**
     * <p>문의내역 답변 조회</p>
     *
     * @param inqId         문의내역 ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId        마지막 항목의 id
     * @param size          한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-13
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CursorPage<InquiryReplyDto>>> getInquiryReplyList(Long inqId, LocalDateTime lastCreatedAt, Long lastId, int size) {
        loaders.inquiry(inqId);

        int limitPlusOne = Math.max(1, size) + 1;
        List<InquiryReplyDto> replyList = replyMapper.getInquiryReplyList(inqId, lastCreatedAt, lastId, limitPlusOne);

        boolean hasMore = replyList.size() > size;
        if (hasMore) {
            replyList = replyList.subList(0, size);
        }

        Cursor next = null;
        if (hasMore && !replyList.isEmpty()) {
            InquiryReplyDto last = replyList.getLast();
            next = new Cursor(last.getCreatedAt(), last.getInqId());
        }

        return ResponseEntity.ok(ResponseDto.success(200, "댓글 목록 조회 성공", CursorPage.of(replyList, next)));
    }

    /**
     * <p>문의내역 답변 등록</p>
     *
     * @param inqId 문의내역 ID
     * @param dto   IqrReplyCreateRequestDto
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-13
     */
    @Override
    public ResponseEntity<ResponseDto<InquiryReplyDto>> createInquiryReply(Long inqId, IqrReplyCreateRequestDto dto) {
        Inquiry existingInquiry = loaders.inquiry(inqId);

        String content = (dto.getContent() == null ? "" : dto.getContent().trim());
        requireHasText(content, ContentRequiredException::new);
        //TODO: 문자 길이 체크

        Reply reply = Reply.builder()
                .inqId(inqId)
                .content(content)
                .code("IQ")
                .build();

        replyMapper.createInquiryReply(reply);
        notificationPublisher.publish(existingInquiry.getUserId(), NotificationType.INQUIRY_ANSWERED, existingInquiry.getTitle(), inqId);
        return ResponseEntity.ok(ResponseDto.success(200, "댓글 등록 성공", null));
    }

    /**
     * <p>Q&A 답변 등록</p>
     *
     * @param qnaId 커뮤니티 ID
     * @param dto   QnaReplyCreateRequestDto
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-14
     */
    @Override
    public ResponseEntity<ResponseDto<QnaReplyDto>> createQnaReply(Long qnaId, Long creatorId, QnaReplyCreateRequestDto dto) {
        Qna existingQna = loaders.qna(qnaId);
        loaders.creator(creatorId);

        String content = (dto.getContent() == null ? "" : dto.getContent().trim());
        requireHasText(content, ContentRequiredException::new);
        //TODO: 문자 길이 체크

        Reply reply = Reply.builder()
                .qnaId(qnaId)
                .content(content)
                .code("QnA")
                .creatorId(creatorId)
                .build();

        replyMapper.createQnaReply(reply);
        notificationPublisher.publish(existingQna.getUserId(), NotificationType.QNA_REPLY, existingQna.getTitle(), qnaId);
        return ResponseEntity.ok(ResponseDto.success(200, "댓글 등록 성공", null));
    }

}
