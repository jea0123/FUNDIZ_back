package com.example.funding.service.validator;

import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import com.example.funding.mapper.*;
import com.example.funding.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectValidator {

    private final CategoryMapper categoryMapper;
    private final RewardMapper rewardMapper;
    private final TagMapper tagMapper;
    private final CreatorMapper creatorMapper;

    private static final int MIN_TITLE_LEN = 2;
    private static final int MAX_TITLE_LEN = 255;
    private static final int MIN_CONTENT_LEN = 30;
    private static final int MAX_CONTENT_LEN = 3000;
    private static final int MAX_THUMBNAIL_LEN = 500;
    private static final int MAX_TAGS = 10;
    private static final int MIN_GOAL = 10_000;
    private static final int MAX_GOAL = 1_000_000_000;
    private static final int MAX_REWARD_NAME_LEN = 255;
    private static final int MAX_REWARD_CONTENT_LEN = 255;
    private static final int MIN_REWARD_PRICE = 1_000;

    public List<String> validateAll(Long projectId, Project project) {
        List<String> errors = new ArrayList<>();
        validateNotNullByERD(project, errors);
        validateBasics(project, errors);
        validateDates(project, errors);
        validateThumbnail(project, errors);
        validateContent(project, errors);
        validateCategory(project, errors);
        validateCreator(project.getCreatorId(), errors);
        validateTags(projectId, errors);
        validateRewards(projectId, errors);
        return errors;
    }

    private static void validateNotNullByERD(Project project, List<String> errors) {
        if (project.getCreatorId() == null) errors.add("creatorId 는 필수입니다.");
        if (project.getSubctgrId() == null) errors.add("subctgrId 는 필수입니다.");
        if (project.getTitle() == null) errors.add("title 는 필수입니다.");
        if (project.getGoalAmount() == null) errors.add("goalAmount 는 필수입니다.");
        if (project.getCurrAmount() == null) errors.add("currAmount 는 필수입니다.");
        if (project.getStartDate() == null) errors.add("startDate 는 필수입니다.");
        if (project.getEndDate() == null) errors.add("endDate 는 필수입니다.");
        if (project.getContent() == null) errors.add("content 는 필수입니다.");
        if (project.getThumbnail() == null) errors.add("thumbnail 는 필수입니다.");
    }

    private static void validateBasics(Project project, List<String> errors) {
        String title = nvl(project.getTitle());
        if (title.length() < MIN_TITLE_LEN) errors.add("제목이 너무 짧습니다.");
        if (title.length() > MAX_TITLE_LEN) errors.add("제목이 255자를 초과합니다.");

        Integer goal = project.getGoalAmount();
        if (goal != null && goal < MIN_GOAL) errors.add("목표 금액이 너무 낮습니다(최소 1만원).");
        if (goal != null && goal > MAX_GOAL) errors.add("목표 금액이 허용 범위를 초과합니다(최대 10억).");
    }

    private static void validateDates(Project project, List<String> errors) {
        if (project.getStartDate() == null || project.getEndDate() == null) {
            errors.add("시작일/종료일이 비어 있습니다.");
            return;
        }
        LocalDate start = project.getStartDate();
        LocalDate end = project.getEndDate();

        if (!start.isBefore(end)) errors.add("시작일은 종료일 이전이어야 합니다.");
        if (start.isBefore(LocalDate.now()))  errors.add("시작일은 오늘 이후여야 합니다.");

        long days = ChronoUnit.DAYS.between(start, end);
        if (days < 7) errors.add("펀딩 기간은 최소 7일입니다.");
        if (days > 60) errors.add("펀딩 기간은 최대 60일입니다.");
    }

    private static void validateThumbnail(Project project, List<String> errors) {
        String thumb = nvl(project.getThumbnail());
        if (thumb.isBlank()) {
            errors.add("대표 이미지가 필요합니다.");
        } else {
            if (thumb.length() > MAX_THUMBNAIL_LEN) errors.add("대표 이미지 경로가 500자를 초과합니다.");
            String lower = thumb.toLowerCase(Locale.ROOT);
            if (!(lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png"))) {
                errors.add("대표 이미지는 jpg/jpeg/png를 권장합니다.");
            }
        }
    }

    private static void validateContent(Project project, List<String> errors) {
        String content = nvl(project.getContent().trim());
        if (content.length() < MIN_CONTENT_LEN) errors.add("프로젝트 소개 글이 너무 짧습니다.(최소 " + MIN_CONTENT_LEN + "자)");
        if (content.length() > MAX_CONTENT_LEN) errors.add("프로젝트 소개 글이 3000자를 초과합니다.");
    }

    private void validateCategory(Project project, List<String> errors) {
        if (project.getSubctgrId() == null) {
            errors.add("서브카테고리가 선택되지 않았습니다.");
            return;
        }
        Subcategory existing = categoryMapper.findSubcategoryById(project.getSubctgrId());
        if (existing == null) errors.add("존재하지 않는 서브카테고리 입니다.");
    }

    private void validateCreator(Long creatorId, List<String> errors) {
        if (creatorId == null) return;
        Creator creator = creatorMapper.findById(creatorId);
        if (creator == null) {
            errors.add("창작자 정보가 유효하지 않습니다.");
            return;
        }
        if (isBlank(creator.getCreatorName())) errors.add("창작자명이 필요합니다.");
        if (isBlank(creator.getBusinessNum())) errors.add("사업자 등록번호가 필요합니다.");
        if (isBlank(creator.getEmail())) errors.add("연락 가능한 이메일이 필요합니다.");
        if (isBlank(creator.getPhone())) errors.add("연락 가능한 전화번호가 필요합니다.");
    }

    private void validateTags(Long projectId, List<String> errors) {
        List<Tag> tags = tagMapper.getTagList(projectId);
        if (tags == null) return;
        if (tags.size() > MAX_TAGS) errors.add("태그는 최대 " + MAX_TAGS + "개까지 등록 가능합니다.");
        long distinct = tags.stream()
            .map(t -> nvl(t.getTagName())
            .trim().toLowerCase()).distinct()
            .count();
        if (distinct != tags.size()) errors.add("중복된 태그가 있습니다.");
    }

    //DB 기준 리워드 검증
    private void validateRewards(Long projectId, List<String> errors) {
        List<Reward> rewards = rewardMapper.findByProjectId(projectId);
        if (rewards == null || rewards.isEmpty()) {
            errors.add("최소 1개 이상의 리워드가 필요합니다.");
            return;
        }
        validateRewardFields(rewards, errors);
    }

    //DTO용 오버로드 : 필드 검증만 수행(DB 조회 없음)
    public List<String> validateRewardsDtos(List<RewardCreateRequestDto> dtos) {
        List<String> errors = new ArrayList<>();
        if (dtos == null || dtos.isEmpty()) {
            return errors;
        }

        List<Reward> rewards = dtos.stream()
            .filter(Objects::nonNull)
            .map(dto -> Reward.builder()
                .rewardName(safeTrim(dto.getRewardName()))
                .price(dto.getPrice())
                .rewardContent(safeTrim(dto.getRewardContent()))
                .deliveryDate(dto.getDeliveryDate())
                .rewardCnt(dto.getRewardCnt())
                .isPosting(dto.getIsPosting())
                .build())
            .toList();

        validateRewardFields(rewards, errors);
        return errors;
    }

    public List<String> validateRewardDto(RewardCreateRequestDto dto) {
        return validateRewardsDtos(Collections.singletonList(dto));
    }

    //리워드 리스트 자체의 중복/필드 제약 검증 (DB 비의존)
    private void validateRewardFields(List<Reward> rewards, List<String> errors) {
        long distinct = rewards.stream()
            .map(r -> nvl(r.getRewardName()).trim().toLowerCase(Locale.ROOT))
            .distinct()
            .count();
        if (distinct != rewards.size()) errors.add("중복된 리워드명이 있습니다.");

        for (Reward reward : rewards) {
            String name = nvl(reward.getRewardName()).trim();
            if (name.isEmpty()) errors.add("리워드명이 필요합니다.");
            if (name.length() > MAX_REWARD_NAME_LEN) errors.add("리워드명이 255자를 초과합니다.");

            String content = nvl(reward.getRewardContent()).trim();
            if (content.isEmpty()) errors.add("리워드 내용이 필요합니다.");
            if (content.length() > MAX_REWARD_CONTENT_LEN) errors.add("리워드 내용이 255자를 초과합니다.");

            if (reward.getPrice() == null || reward.getPrice() < MIN_REWARD_PRICE) {
                errors.add("리워드 금액은 최소 " + MIN_REWARD_PRICE + "원 이상이어야 합니다.");
            }

            Integer cnt = reward.getRewardCnt();
            if (cnt != null && cnt < 0) errors.add("리워드 수량은 0 이상이어야 합니다."); // 수량 0 이면 품절

            Character posting = reward.getIsPosting();
            if (posting == null) {
                errors.add("배송 필요 여부는 필수입니다.");
            } else {
                char upper = Character.toUpperCase(posting);
                if (upper != 'Y' && upper != 'N') {
                    errors.add("배송 필요 여부는 Y 또는 N 이어야 합니다.");
                } else {
                    reward.setIsPosting(upper);
                }
            }

            if (reward.getDeliveryDate() != null && reward.getDeliveryDate().isBefore(LocalDate.now())) {
                errors.add("배송 예정일은 펀딩 종료일 이후여야 합니다.");
            }
        }
    }

    private static String nvl(String s) { return s == null ? "" : s; }
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
    private static String safeTrim(String s) { return s == null ? null : s.trim(); }
}
