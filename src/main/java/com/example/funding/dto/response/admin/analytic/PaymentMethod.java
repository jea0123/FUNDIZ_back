package com.example.funding.dto.response.admin.analytic;

import com.example.funding.enums.PaymentMethods;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentMethod {
    private PaymentMethods paymentMethod;
    private Long cnt;
}
