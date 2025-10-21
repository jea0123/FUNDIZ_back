package com.example.funding.service.validator;

import com.example.funding.dto.request.address.AddrAddRequestDto;
import com.example.funding.dto.request.address.AddrUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.funding.service.validator.AddressValidationRules.*;

@Component
@RequiredArgsConstructor
public class AddressValidator {
    public void validate(AddrAddRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("배송지 정보가 비어 있습니다.");
        }

        if (isBlank(dto.getAddrName())) throw new IllegalArgumentException("배송지 이름을 입력해주세요.");
        if (isBlank(dto.getRecipient())) throw new IllegalArgumentException("수령인 이름을 입력해주세요.");
        if (isBlank(dto.getPostalCode())) throw new IllegalArgumentException("우편번호를 입력해주세요.");
        if (isBlank(dto.getRoadAddr())) throw new IllegalArgumentException("도로명 주소를 입력해주세요.");
        if (isBlank(dto.getRecipientPhone())) throw new IllegalArgumentException("수령인 전화번호를 입력해주세요.");

        if (!POSTAL_PATTERN.matcher(dto.getPostalCode()).matches())
            throw new IllegalArgumentException("우편번호는 5자리 숫자여야 합니다.");

        if (!PHONE_PATTERN.matcher(dto.getRecipientPhone()).matches())
            throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다. 예: 010-1234-5678 / 02-123-4567");

        if (dto.getAddrName().length() > MAX_ADDR_NAME_LEN)
            throw new IllegalArgumentException("배송지 이름은 " + MAX_ADDR_NAME_LEN + "자 이내로 입력해주세요.");

        if (dto.getRoadAddr().length() > MAX_ROAD_ADDR_LEN)
            throw new IllegalArgumentException("도로명 주소는 " + MAX_ROAD_ADDR_LEN + "자 이내로 입력해주세요.");

        if (dto.getDetailAddr() != null && dto.getDetailAddr().length() > MAX_DETAIL_ADDR_LEN)
            throw new IllegalArgumentException("상세 주소는 " + MAX_DETAIL_ADDR_LEN + "자 이내로 입력해주세요.");
    }

    public void validateUpdate(AddrUpdateRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("수정할 주소 정보가 비어 있습니다.");
        }
        if (dto.getAddrId() == null || dto.getAddrId() <= 0) {
            throw new IllegalArgumentException("주소 ID가 올바르지 않습니다.");
        }

        // 기존의 공통 검증 로직 호출
        validateCommon(dto.getAddrName(), dto.getRecipient(), dto.getPostalCode(),
                dto.getRoadAddr(), dto.getDetailAddr(), dto.getRecipientPhone());
    }

    public void validateCommon(String addrName, String recipient, String postalCode,
                               String roadAddr, String detailAddr, String recipientPhone) {
        if (AddressValidationRules.isBlank(addrName)) throw new IllegalArgumentException("배송지 이름을 입력해주세요.");
        if (AddressValidationRules.isBlank(recipient)) throw new IllegalArgumentException("수령인 이름을 입력해주세요.");
        if (AddressValidationRules.isBlank(postalCode)) throw new IllegalArgumentException("우편번호를 입력해주세요.");
        if (AddressValidationRules.isBlank(roadAddr)) throw new IllegalArgumentException("도로명 주소를 입력해주세요.");
        if (AddressValidationRules.isBlank(recipientPhone)) throw new IllegalArgumentException("전화번호를 입력해주세요.");

        if (!AddressValidationRules.POSTAL_PATTERN.matcher(postalCode).matches())
            throw new IllegalArgumentException("우편번호는 5자리 숫자여야 합니다.");

        if (!AddressValidationRules.PHONE_PATTERN.matcher(recipientPhone).matches())
            throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다. 예: 010-1234-5678");

        if (addrName.length() > AddressValidationRules.MAX_ADDR_NAME_LEN)
            throw new IllegalArgumentException("배송지 이름은 " + AddressValidationRules.MAX_ADDR_NAME_LEN + "자 이내로 입력해주세요.");
        if (roadAddr.length() > AddressValidationRules.MAX_ROAD_ADDR_LEN)
            throw new IllegalArgumentException("도로명 주소는 " + AddressValidationRules.MAX_ROAD_ADDR_LEN + "자 이내로 입력해주세요.");
        if (detailAddr != null && detailAddr.length() > AddressValidationRules.MAX_DETAIL_ADDR_LEN)
            throw new IllegalArgumentException("상세 주소는 " + AddressValidationRules.MAX_DETAIL_ADDR_LEN + "자 이내로 입력해주세요.");
    }
}

