package com.tenpo.jarvis.dto;

import lombok.*;

import java.math.BigDecimal;


@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OperationResponseDTO {

    @Getter
    @Setter
    private BigDecimal number1;

    @Getter
    @Setter
    private BigDecimal number2;

    @Getter
    @Setter
    private BigDecimal percentage;

    @Getter
    @Setter
    private BigDecimal result;

}
