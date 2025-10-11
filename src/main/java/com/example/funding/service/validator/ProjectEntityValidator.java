package com.example.funding.service.validator;

import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import com.example.funding.dto.response.category.SubcategoryWithParentDto;
import com.example.funding.mapper.*;
import com.example.funding.model.Project;
import com.example.funding.model.Reward;
import com.example.funding.model.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static com.example.funding.service.validator.ValidationRules.*;
import static com.example.funding.service.validator.ValidationRules.MAX_CONTENT_LEN;
import static com.example.funding.service.validator.ValidationRules.MAX_DAYS;
import static com.example.funding.service.validator.ValidationRules.MIN_CONTENT_LEN;
import static com.example.funding.service.validator.ValidationRules.MIN_DAYS;
import static com.example.funding.service.validator.ValidationRules.MIN_GOAL_AMOUNT;
import static com.example.funding.service.validator.ValidationRules.MIN_START_LEAD_DAYS;
import static com.example.funding.service.validator.ValidationRules.daysInclusive;
import static com.example.funding.service.validator.ValidationRules.nvl;

@Component
@RequiredArgsConstructor
public class ProjectEntityValidator {
    private final CategoryMapper categoryMapper;
    private final RewardMapper rewardMapper;
    private final TagMapper tagMapper;
    private final CreatorMapper creatorMapper;
    private final UserMapper userMapper;

    private final ProjectInputValidator inputValidator;

    private static final ZoneId ZONE_SEOUL = ZoneId.of("Asia/Seoul");

    /**
     * 기본 필드 검증
     */
    public void validateBasicsFromDb(Project p, List<String> errors) {
        inputValidator.validateBasics(p.getTitle(), p.getContent(), p.getGoalAmount(),
                p.getStartDate(), p.getEndDate(), true, errors);

        LocalDate today = LocalDate.now(ZONE_SEOUL);
        if (p.getStartDate() != null && p.getStartDate().isBefore(today)) {
            errors.add("시작일이 이미 지났습니다. 일정을 조정하세요.");
        }
    }

    /**
     * 카테고리 검증: ID/형식
     */
    public void validateCategoryFromDb(@Nullable Long subctgrId, boolean required, List<String> errors) {
        inputValidator.validateCategoryBasic(subctgrId, required, errors);
        if (subctgrId == null) return;

        SubcategoryWithParentDto dto = categoryMapper.getSubcategoryWithParent(subctgrId);
        if (dto == null) {
            errors.add("유효하지 않은 세부카테고리입니다.");
            return;
        }
        if (dto.getParentCtgrId() == null) {
            errors.add("세부카테고리의 상위 카테고리가 유효하지 않습니다.");
        }
    }

    /**
     * 태그 검증
     */
    public void validateTagsFromDb(Long projectId, List<String> errors) {
        // 문자열 리스트 변환
        List<String> tags = Optional.ofNullable(tagMapper.getTagList(projectId))
            .orElseGet(Collections::emptyList)
            .stream()
            .map(Tag::getTagName)
            .toList();

        inputValidator.validateTags(tags, errors);
    }

    /**
     * 리워드 검증
     */
    public void validateRewardsFromDb(Long projectId, LocalDate endDate, List<String> errors) {
        List<Reward> rewards = Optional.ofNullable(rewardMapper.getRewardList(projectId))
            .orElseGet(Collections::emptyList);

        if (rewards.isEmpty()) {
            errors.add("최소 1개 이상의 리워드가 필요합니다.");
            return;
        }

        // 정규화/중복
        Set<String> seen = new HashSet<>();
        boolean hasDup = false;

        for (Reward r : rewards) {
            errors.addAll(inputValidator.validateRewardFieldCore(
                r.getRewardName(),
                r.getRewardContent(),
                r.getPrice(),
                r.getRewardCnt(),
                r.getIsPosting(),
                r.getDeliveryDate(),
                endDate
            ));

            String key = normRewardName(nvl(r.getRewardName()));
            if (!seen.add(key)) hasDup = true;
        }

        if (hasDup) errors.add("중복된 리워드명이 있습니다.");
    }

    /**
     * 창작자 프로필 검증
     */
    public void validateCreatorProfile(Long creatorId, List<String> errors) {
        boolean ok = creatorMapper.hasRequiredCreatorProfile(creatorId);
        if (!ok) {
            errors.add("창작자 프로필 필수 항목이 미완성입니다. (창작자명/사업자번호/이메일/전화번호)");
        }

        if (Boolean.TRUE.equals(userMapper.suspendedCreator(creatorId))) {
            errors.add("정지된 창작자는 프로젝트 등록/수정이 불가합니다.");
        }
    }
}
