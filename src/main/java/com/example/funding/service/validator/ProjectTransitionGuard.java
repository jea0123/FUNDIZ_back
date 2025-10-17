package com.example.funding.service.validator;

import com.example.funding.mapper.*;
import com.example.funding.model.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ProjectTransitionGuard {

    private final ProjectMapper projectMapper;
    private final CreatorMapper creatorMapper;

    private final ProjectEntityValidator entityValidator;

    public void assertCanCreate(Long creatorId) {
        ensureCreatorExistsOrThrow(creatorId);
    }

    public void assertCanUpdate(Long projectId, Long creatorId) {
        ensureProjectOwner(projectId, creatorId);
        requireDraft(projectId);
    }

    public void assertCanDelete(Long projectId, Long creatorId) {
        ensureProjectOwner(projectId, creatorId);
        requireDraft(projectId);
    }

    /** 제출/심사승인 공통 */
    private List<String> collectValidationErrors(Long projectId) {
        Project p = getProjectOrThrow(projectId);

        List<String> errors = new ArrayList<>();
        entityValidator.validateCategoryFromDb(p.getSubctgrId(), true, errors);
        entityValidator.validateBasicsFromDb(p, errors);
        // TODO: 대표이미지 검증 추가
        entityValidator.validateTagsFromDb(projectId, errors);
        entityValidator.validateRewardsFromDb(projectId, p.getEndDate(), errors);

        return errors;
    }

    /**
     * DRAFT -> VERIFYING 제출 가능 여부
     */
    public void assertCanSubmit(Long projectId, Long creatorId) {
        ensureProjectOwner(projectId, creatorId);
        requireDraft(projectId);

        List<String> errors = collectValidationErrors(projectId);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
        }
    }

    /**
     * VERIFYING -> UPCOMING 심사 승인 가능 여부
     */
    public void assertCanApprove(Long projectId) {
        requireVerifying(projectId);

        List<String> errors = collectValidationErrors(projectId);
        if (!errors.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join("; ", errors));
        }
    }

    /** 프로젝트 가져오기 */
    private Project getProjectOrThrow(Long projectId) {
        Project p = projectMapper.findById(projectId);
        if (p == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다.");
        return p;
    }

    /** 프로젝트 존재 + 소유자 일치 보장 */
    public void ensureProjectOwner(Long projectId, Long creatorId) {
        Project p = getProjectOrThrow(projectId);
        if (creatorId == null || !Objects.equals(p.getCreatorId(), creatorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 프로젝트의 소유자가 아닙니다.");
        }
    }

    /** 상태 조회 코어 */
    private String getStatusOrThrow(Long projectId) {
        String status = projectMapper.getStatus(projectId);
        if (status == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "프로젝트 상태를 조회할 수 없습니다.");
        return status;
    }

    /** 상태 코어 가드 */
    private void requireStatusInInternal(Long projectId, String errorMessage, String... allowedStatuses) {
        String status = getStatusOrThrow(projectId);
        for (String s : allowedStatuses) {
            if (status.equalsIgnoreCase(s)) return;
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, errorMessage);
    }

    /** 상태가 허용 목록 중 하나인지 */
    public void requireStatusIn(Long projectId, String... allowedStatuses) {
        requireStatusInInternal(projectId, "현재 상태에서는 허용되지 않는 작업입니다.", allowedStatuses);
    }

    /** DRAFT 상태 강제 */
    public void requireDraft(Long projectId) {
        requireStatusInInternal(projectId, "DRAFT 상태에서만 수행할 수 있습니다.", "DRAFT");
    }

    /** VERIFYING 상태 강제 */
    public void requireVerifying(Long projectId) {
        requireStatusInInternal(projectId, "VERIFYING 상태에서만 수행할 수 있습니다.", "VERIFYING");
    }

    public void ensureCreatorExistsOrThrow(Long creatorId) {
        if (creatorId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "창작자 ID가 필요합니다.");
        }
        if (creatorMapper.existsCreator(creatorId) <= 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 창작자입니다.");
        }
    }
}
