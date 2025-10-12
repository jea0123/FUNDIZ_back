package com.example.funding.service.impl;

import com.example.funding.common.Cursor;
import com.example.funding.common.CursorPage;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.ReplyCreateRequestDto;
import com.example.funding.dto.response.project.ReplyDto;
import com.example.funding.mapper.CommunityMapper;
import com.example.funding.mapper.ReplyMapper;
import com.example.funding.model.Reply;
import com.example.funding.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReplyServiceImpl implements ReplyService {

    private final ReplyMapper replyMapper;
    private final CommunityMapper communityMapper;

    /**
     * <p>프로젝트 상세 페이지 - 커뮤니티 댓글 목록 조회</p>
     *
     * @param cmId 커뮤니티 ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId 마지막 항목의 id
     * @param size 한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-12
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CursorPage<ReplyDto>>> getReplyList(Long cmId, LocalDateTime lastCreatedAt, Long lastId, int size) {
        if (cmId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cmId가 필요합니다.");
        }
        if (communityMapper.existsCommunityById(cmId) <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "커뮤니티 글이 없습니다.");
        }

        int limitPlusOne = Math.max(1, size) + 1;
        List<ReplyDto> replyList = replyMapper.getReplyList(cmId, lastCreatedAt, lastId, limitPlusOne);

        boolean hasMore = replyList.size() > size;
        if (hasMore) {
            replyList = replyList.subList(0, size);
        }

        Cursor next = null;
        if (hasMore && !replyList.isEmpty()) {
            ReplyDto last = replyList.getLast();
            next = new Cursor(last.getCreatedAt(),last.getCmId());
        }

        return ResponseEntity.ok(ResponseDto.success(200, "댓글 목록 조회 성공", CursorPage.of(replyList, next)));
    }

    /**
     * <p>프로젝트 상세 페이지 - 커뮤니티 댓글 등록</p>
     *
     * @param cmId 커뮤니티 ID
     * @param dto ReplyCreateRequestDto
     * @param userId 사용자 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-13
     */
    @Override
    public ResponseEntity<ResponseDto<ReplyDto>> createCommunityReply(Long cmId, ReplyCreateRequestDto dto, Long userId) {
        if (cmId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cmId가 필요합니다.");
        }
        //TODO: userId 체크
        if (communityMapper.existsCommunityById(cmId) <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "커뮤니티 글이 없습니다.");
        }

        String content = (dto.getContent() == null ? "" : dto.getContent().trim());
        if (content.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "내용은 필수입니다.");
        }
        //TODO: 문자 길이 체크

        Reply reply = Reply.builder()
            .cmId(cmId)
            .userId(userId)
            .content(content)
            .isSecret(dto.getIsSecret())
            .code("CM")
            .build();

        int result = replyMapper.createCommunityReply(reply);
        if (result != 1 || reply.getReplyId() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 등록 실패");
        }

        return ResponseEntity.ok(ResponseDto.success(200, "댓글 등록 성공", null));
    }
}
