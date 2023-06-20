package com.tenpo.jarvis.service.operation;

import com.tenpo.jarvis.dto.OperationResponseDTO;

import java.math.BigDecimal;

public interface OperationService {
    OperationResponseDTO calculate(final BigDecimal number1, final BigDecimal number2);

}
