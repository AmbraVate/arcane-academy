package com.ambravate.arcane.academy.payment.dto;

import com.ambravate.arcane.academy.payment.PlanType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckoutRequest {

    @NotNull(message = "planType is required")
    private PlanType planType;
}
