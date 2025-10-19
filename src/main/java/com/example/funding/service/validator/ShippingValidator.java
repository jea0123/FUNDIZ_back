package com.example.funding.service.validator;

import com.example.funding.dto.request.shipping.ShippingStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.example.funding.service.validator.ValidationRules.*;

@Component
@RequiredArgsConstructor
public class ShippingValidator {
    /**
     * <p>배송 상태 변경 및 운송장 번호 검증</p>
     * @param currentStatus 현재 배송 상태
     * @param dto 변경 요청 DTO
     */
    public void validateTransition(String currentStatus, ShippingStatusDto dto) {
        String nextStatus = dto.getShippingStatus();

        // 상태 전환 가능 여부 검증
        List<String> allowedNext = SHIPPING_ALLOWED_TRANSITIONS.getOrDefault(currentStatus, List.of());
        if (!allowedNext.contains(nextStatus)) {
            throw new IllegalArgumentException(String.format(
                    "현재 상태(%s)에서는 '%s'(으)로 변경할 수 없습니다.",
                    SHIPPING_STATUS_LABEL.getOrDefault(currentStatus, currentStatus),
                    SHIPPING_STATUS_LABEL.getOrDefault(nextStatus, nextStatus)
            ));
        }

        //  운송장 번호 필수 검증 (배송 시작 / 완료 시)
        if (nextStatus.equals("SHIPPED") || nextStatus.equals("DELIVERED")) {
            if (isBlank(dto.getTrackingNum()) || !TRACKING_NUM_PATTERN.matcher(dto.getTrackingNum()).matches()) {
                throw new IllegalArgumentException(
                        String.format("%s 상태로 변경하려면 10~14자리 숫자의 운송장 번호가 필요합니다.",
                                SHIPPING_STATUS_LABEL.get(nextStatus))
                );
            }
        }

        //  배송 시작 이후 운송장 번호 변경 불가
        if (currentStatus.equals("SHIPPED") || currentStatus.equals("DELIVERED")
                || currentStatus.equals("CANCELED") || currentStatus.equals("FAILED")) {

            if (dto.getTrackingNum() != null && !dto.getTrackingNum().equals(dto.getOriginalTrackingNum())) {
                throw new IllegalArgumentException("배송 시작 이후에는 운송장 번호를 수정할 수 없습니다.");
            }
        }
    }
}
