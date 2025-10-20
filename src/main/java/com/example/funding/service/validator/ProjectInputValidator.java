package com.example.funding.service.validator;

import com.example.funding.dto.request.creator.ProjectCreateRequestDto;
import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.example.funding.service.validator.ProjectValidationRules.*;

@Component
@RequiredArgsConstructor
public class ProjectInputValidator {

    /**
     * 프로젝트 생성 요청 DTO 검증
     */
    public List<String> validateProjectCreate(ProjectCreateRequestDto dto) {
        List<String> errors = new ArrayList<>();

        validateCategoryBasic(dto.getSubctgrId(), true, errors);
        validateBasics(dto.getTitle(), dto.getContent(), dto.getGoalAmount(),
                dto.getStartDate(), dto.getEndDate(), true, errors);

        validateThumbnail(dto.getThumbnail(), true, errors);
        validateThumbnailUrl(dto.getThumbnailUrl(), false, errors);
        validateBusinessDoc(dto.getBusinessDoc(), false, errors);

        validateTags(dto.getTagList(), errors);
        if (dto.getRewardList() != null && !dto.getRewardList().isEmpty()) {
            errors.addAll(validateRewards(dto.getRewardList(), dto.getEndDate()));
        }

        return errors;
    }

    /**
     * 프로젝트 수정 요청 DTO 검증
     */
    public List<String> validateProjectUpdate(ProjectCreateRequestDto dto) {
        List<String> errors = new ArrayList<>();

        validateCategoryBasic(dto.getSubctgrId(), false, errors);
        validateBasics(dto.getTitle(), dto.getContent(), dto.getGoalAmount(),
            dto.getStartDate(), dto.getEndDate(), false, errors);

        validateThumbnail(dto.getThumbnail(), false, errors);
        validateThumbnailUrl(dto.getThumbnailUrl(), false, errors);
        validateBusinessDoc(dto.getBusinessDoc(), false, errors);

        validateTags(dto.getTagList(), errors);
        if (dto.getRewardList() != null && !dto.getRewardList().isEmpty()) {
            errors.addAll(validateRewards(dto.getRewardList(), dto.getEndDate()));
        }

        return errors;
    }

    private void validateThumbnail(MultipartFile file, boolean required, List<String> errors) {
        boolean hasFile = file != null && !file.isEmpty();

        if (required && !hasFile) {
            errors.add("대표이미지 파일은 필수입니다.");
            return;
        }
        if (!hasFile) return;

        String ct = Optional.ofNullable(file.getContentType()).orElse("");
        if (!ALLOWED_IMAGE_TYPES.contains(ct)) {
            errors.add("대표이미지 형식은 JPEG 또는 PNG만 허용됩니다.");
            return;
        }

        try (InputStream is = file.getInputStream()) {
            if (!isImageBySignature(is)) {
                errors.add("대표이미지 파일이 손상되었거나 이미지가 아닙니다.");
                return;
            }
        } catch (Exception e) {
            errors.add("대표이미지 파일을 읽는 중 오류가 발생했습니다.");
            return;
        }
    }

    private void validateThumbnailUrl(String url, boolean required, List<String> errors) {
        String u = nvl(url).trim();
        if (required && u.isEmpty()) {
            errors.add("대표이미지 업로드에 실패했습니다. 다시 시도해주세요.");
            return;
        }
        if (!u.isEmpty() && u.length() > MAX_THUMBNAIL_LEN) {
            errors.add("대표이미지 URL 길이는 최대 " + MAX_THUMBNAIL_LEN + "자 이하여야 합니다.");
        }
    }

    public void validateBusinessDoc(@Nullable MultipartFile file, boolean required, List<String> errors) {
        boolean hasFile = file != null && !file.isEmpty();

        if (required && !hasFile) {
            errors.add("사업자등록증 파일은 필수입니다.");
            return;
        }
        if (!hasFile) return;

        String ct = Optional.ofNullable(file.getContentType()).orElse("");
        boolean isPdf = "application/pdf".equals(ct);
        boolean isImg = ALLOWED_IMAGE_TYPES.contains(ct);
        if (!isPdf && !isImg) {
            errors.add("사업자등록증은 PDF 또는 JPEG/PNG 형식만 허용됩니다.");
            return;
        }

        try (InputStream is = file.getInputStream()) {
            if (isPdf) {
                if (!isPdfBySignature(is)) {
                    errors.add("PDF 파일이 손상되었거나 확장자/형식이 일치하지 않습니다.");
                }
            } else {
                if (!isImageBySignature(is)) {
                    errors.add("이미지 파일이 손상되었거나 지원되지 않습니다.");
                }
            }
        } catch (Exception e) {
            errors.add("사업자등록증 파일을 확인하는 중 오류가 발생했습니다.");
        }
    }

    /**
     * 기본 필드: 제목/본문/금액/기간
     */
    public void validateBasics(@Nullable String title,
                               @Nullable String content,
                               @Nullable Integer goalAmount,
                               @Nullable LocalDateTime startDate,
                               @Nullable LocalDateTime endDate,
                               boolean forCreate,
                               List<String> errors) {

        if (forCreate || title != null) {
            String t = nvl(title).trim();
            if (t.length() < MIN_TITLE_LEN || t.length() > MAX_TITLE_LEN) {
                errors.add("제목 길이는 " + MIN_TITLE_LEN + "~" + MAX_TITLE_LEN + "자여야 합니다.");
            }
        }

        if (forCreate || content != null) {
            String c = nvl(content).trim();
            if (c.length() < MIN_CONTENT_LEN || c.length() > MAX_CONTENT_LEN) {
                errors.add("본문 길이는 " + MIN_CONTENT_LEN + "~" + MAX_CONTENT_LEN + "자여야 합니다.");
            }
        }

        if (forCreate) {
            if (goalAmount == null) {
                errors.add("목표 금액은 필수입니다.");
            } else if (goalAmount < MIN_GOAL_AMOUNT || goalAmount > MAX_GOAL_AMOUNT) {
                errors.add("목표 금액은 최소 " + MIN_GOAL_AMOUNT + "원, 최대 " + MAX_GOAL_AMOUNT + "원 이하여야 합니다.");
            }
        } else {
            // 프로젝트 수정인 경우
            if (goalAmount != null && (goalAmount < MIN_GOAL_AMOUNT || goalAmount > MAX_GOAL_AMOUNT)) {
                errors.add("목표 금액은 최소 " + MIN_GOAL_AMOUNT + "원, 최대 " + MAX_GOAL_AMOUNT + "원 이하여야 합니다.");
            }
        }

        if (forCreate) {
            if (startDate == null || endDate == null) {
                errors.add("펀딩 시작/종료일은 필수입니다.");
                return;
            }
        }
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                errors.add("펀딩 시작일은 종료일보다 이후일 수 없습니다.");
                return;
            }
            long days = daysInclusive(startDate, endDate);
            if (days < MIN_DAYS || days > MAX_DAYS) {
                errors.add("펀딩 기간은 " + MIN_DAYS + "~" + MAX_DAYS + "일이어야 합니다.");
            }

            LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
            if (startDate.isBefore(today.plusDays(MIN_START_LEAD_DAYS).atStartOfDay())) {
                errors.add("시작일은 오늘로부터 최소 " + MIN_START_LEAD_DAYS + "일 이후여야 합니다.");
            }
        }
    }

    /**
     * 카테고리 ID/형식 검증
     */
    public void validateCategoryBasic(@Nullable Long subctgrId, boolean required, List<String> errors) {
        if (subctgrId == null) {
            if (required) errors.add("세부카테고리는 필수입니다.");
            return;
        }
        if (subctgrId <= 0) {
            errors.add("유효하지 않은 세부카테고리입니다.");
        }
    }

    /**
     * 태그 생성/수정 요청 DTO 검증
     */
    public void validateTags(@Nullable List<String> tags, List<String> errors) {
        if (tags == null) return;

        // 정규화/개수 제한/중복
        Map<String, String> seen = new LinkedHashMap<>();
        boolean hasDup = false;
        int originalCount = 0;

        for (String tag : tags) {
            if (tag == null) continue;
            String display = normTagDisplay(tag);
            String key = normTagKey(tag);

            if (display.isEmpty() || key.isEmpty()) continue;
            originalCount++;

            if (display.length() < MIN_TAG_LEN) errors.add("태그 길이는 최소 " + MIN_TAG_LEN + "자입니다.");
            if (display.length() > MAX_TAG_LEN) errors.add("태그 길이는 최대 " + MAX_TAG_LEN + "자입니다.");

            if (!TAG_PATTERN.matcher(display).matches())
                errors.add("태그 '" + display + "'에 허용되지 않는 문자가 포함되어 있습니다.");

            if (seen.putIfAbsent(key, display) != null) hasDup = true;
        }

        if (originalCount > MAX_TAGS) errors.add("태그는 최대 " + MAX_TAGS + "개까지 가능합니다.");
        if (seen.size() > MAX_TAGS) errors.add("(정규화) 태그는 최대 " + MAX_TAGS + "개까지 가능합니다.");
        if (hasDup) errors.add("중복된 태그가 있습니다.");
    }

    /**
     * 리워드 검증 코어
     */
    public List<String> validateRewardFieldCore(String rewardName,
                                                String rewardContent,
                                                Long price,
                                                Integer rewardCnt,
                                                Character isPosting,
                                                LocalDateTime deliveryDate,
                                                @Nullable LocalDateTime endDate) {

        List<String> errors = new ArrayList<>();

        String name = nvl(rewardName).trim();
        if (name.isEmpty()) errors.add("리워드명은 필수입니다.");
        if (name.length() > MAX_REWARD_NAME_LEN)
            errors.add("리워드명은 " + MAX_REWARD_NAME_LEN + "자를 초과할 수 없습니다.");

        String content = nvl(rewardContent).trim();
        if (content.isEmpty()) errors.add("리워드 내용은 필수입니다.");
        if (content.length() > MAX_REWARD_CONTENT_LEN)
            errors.add("리워드 내용은 " + MAX_REWARD_CONTENT_LEN + "자를 초과할 수 없습니다.");

        if (price  == null || price  < MIN_REWARD_PRICE)
            errors.add("리워드 가격은 최소 " + MIN_REWARD_PRICE + "원 이상이어야 합니다.");

        // null=무제한
        if (rewardCnt != null && rewardCnt <= 0)
            errors.add("리워드 수량은 1 이상이어야 합니다. 비워두면 무제한으로 설정됩니다.");

        if (isPosting == null || (isPosting != 'Y' && isPosting != 'N'))
            errors.add("배송 필요 여부는 Y 또는 N 이어야 합니다.");

        if (deliveryDate  == null) {
            errors.add("배송예정일을 입력해주세요.");
        } else if (endDate != null && !deliveryDate.isAfter(endDate)) {
            errors.add("배송예정일은 펀딩 종료일 이후여야 합니다.");
        }

        return errors;
    }

    /**
     * 리워드 단건 생성/수정 요청 DTO 검증
     */
    public List<String> validateRewardFields(RewardCreateRequestDto dto, @Nullable LocalDateTime endDate) {
        return validateRewardFieldCore(
            dto.getRewardName(),
            dto.getRewardContent(),
            dto.getPrice(),
            dto.getRewardCnt(),
            dto.getIsPosting(),
            dto.getDeliveryDate(),
            endDate);
    }

    /**
     * 리워드 목록 생성/수정 요청 DTO 검증
     */
    public List<String> validateRewards(List<RewardCreateRequestDto> list, @Nullable LocalDateTime endDate) {
        List<String> errors = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            errors.add("최소 1개 이상의 리워드가 필요합니다.");
            return errors;
        }

        for (RewardCreateRequestDto dto : list) {
            errors.addAll(validateRewardFields(dto, endDate));
        }

        // 정규화/중복
        Set<String> seen = new HashSet<>();
        Set<String> dup = new LinkedHashSet<>();

        for (RewardCreateRequestDto dto : list) {
            String name = normRewardName(dto.getRewardName());
            if (name.isEmpty()) continue;
            if (!seen.add(name)) dup.add(name);
        }
        if (!dup.isEmpty()) errors.add("중복된 리워드명이 있습니다.");

        return errors;
    }

    private boolean isImageBySignature(InputStream is) throws Exception {
        is.mark(16);
        byte[] head = is.readNBytes(12);
        is.reset();
        // JPEG: FF D8 FF
        if (head.length >= 3 && (head[0] & 0xFF) == 0xFF && (head[1] & 0xFF) == 0xD8 && (head[2] & 0xFF) == 0xFF) return true;
        // PNG: 89 50 4E 47 0D 0A 1A 0A
        if (head.length >= 8 && (head[0] & 0xFF) == 0x89 && head[1]=='P' && head[2]=='N' && head[3]=='G') return true;
        // (WebP 필요 시 추가: "RIFF....WEBP")
        return false;
    }

    private boolean isPdfBySignature(InputStream is) throws Exception {
        is.mark(8);
        byte[] head = is.readNBytes(5);
        is.reset();
        // "%PDF-"
        return head.length >= 5 && head[0]=='%' && head[1]=='P' && head[2]=='D' && head[3]=='F' && head[4]=='-';
    }
}
