package com.tenpo.jarvis.service;

import com.tenpo.jarvis.component.CacheStorage;
import com.tenpo.jarvis.controller.exception.CacheException;
import com.tenpo.jarvis.controller.exception.MaxRetryException;
import com.tenpo.jarvis.dto.OperationResponseDTO;
import com.tenpo.jarvis.service.external.MockNumberService;
import com.tenpo.jarvis.service.operation.OperationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OperationServiceTest {

    @Mock
    CacheStorage cacheStorage;

    @Mock
    MockNumberService mockNumberService;

    @InjectMocks
    OperationServiceImpl operationService;

    @Test
    public void test_sum_numbers_with_percentage() throws ExecutionException {
        when(mockNumberService.getRandomNumber()).thenReturn(new BigDecimal(10));
        OperationResponseDTO responseDTO = operationService.calculate(new BigDecimal(5), new BigDecimal(5));

        assertEquals(responseDTO.getResult(), new BigDecimal("11.0"));
    }

    @Test
    public void test_get_percentage_value_from_cached() throws ExecutionException {
        when(operationService.getExternalRandomNumber()).thenThrow(MaxRetryException.class);
        when(cacheStorage.getFromCache(anyString())).thenReturn(new BigDecimal(10));
        BigDecimal percentageToApply = operationService.getPercentageToApply();

        assertEquals(percentageToApply, new BigDecimal(10));
    }

    @Test
    public void test_value_no_persisted_in_cached() throws ExecutionException {
        CacheException thrown = assertThrows(CacheException.class, () -> {
            when(operationService.getExternalRandomNumber()).thenThrow(MaxRetryException.class);
            when(cacheStorage.getFromCache(anyString())).thenThrow(NumberFormatException.class);
            operationService.getPercentageToApply();
        });
    }

    @Test
    public void test_external_max_retry() throws ExecutionException {
        MaxRetryException thrown = assertThrows(MaxRetryException.class, () -> {
            when(mockNumberService.getRandomNumber()).thenThrow(RestClientException.class);
            operationService.getExternalRandomNumber();
        });
    }
}
