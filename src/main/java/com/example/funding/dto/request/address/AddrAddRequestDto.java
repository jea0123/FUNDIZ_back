    package com.example.funding.dto.request.address;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.Setter;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public class AddrAddRequestDto {
        private String addrName;
        private String recipient;
        private String postalCode;
        private String roadAddr;
        private String detailAddr;
        private String recipientPhone;
        private String isDefault;

    }
