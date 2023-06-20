package com.tenpo.jarvis.service.operation;

import com.tenpo.jarvis.component.CacheStorage;
import com.tenpo.jarvis.controller.exception.CacheException;
import com.tenpo.jarvis.controller.exception.MaxRetryException;
import com.tenpo.jarvis.dto.OperationResponseDTO;
import com.tenpo.jarvis.service.external.MockNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import static com.tenpo.jarvis.config.ErrorMessage.ERROR_GET_VALUE_FROM_CACHE;
import static com.tenpo.jarvis.config.ErrorMessage.ERROR_MAX_RETRIES;

@Service
public class OperationServiceImpl implements OperationService {

    @Autowired
    private CacheStorage cacheStorage;

    @Autowired
    private MockNumberService mockNumberService;

    private static final int MAX_RETRY = 3;

    @Override
    public OperationResponseDTO calculate(BigDecimal number1, BigDecimal number2) {
        final BigDecimal percentage = getPercentageToApply();
        final BigDecimal sumOfNumbers = number1.add(number2);
        final BigDecimal result = sumOfNumbers.multiply(new BigDecimal(1)
                .add(percentage.divide(new BigDecimal(100))));

        return OperationResponseDTO.builder()
                .number1(number1)
                .number2(number2)
                .percentage(percentage)
                .result(result)
                .build();
    }

    public BigDecimal getPercentageToApply() {
        BigDecimal percentage;
        try {
            percentage = getExternalRandomNumber();
            cacheStorage.persist("percentage", percentage.toString());
        } catch (MaxRetryException e){
            percentage = getPercentageFromCache();
        }
        return percentage;
    }

    private BigDecimal getPercentageFromCache() {
        final BigDecimal percentage;
        try{
            percentage = cacheStorage.getFromCache("percentage");
        } catch(ExecutionException e2){
            throw new CacheException(ERROR_GET_VALUE_FROM_CACHE);
        } catch(NumberFormatException e3){
            throw new CacheException(ERROR_GET_VALUE_FROM_CACHE);
        }
        return percentage;
    }

    public BigDecimal getExternalRandomNumber() {
        int count = 0;
        while(true) {
            try {
                return mockNumberService.getRandomNumber();
            } catch (RestClientException e) {
                count++;
                if (count == MAX_RETRY) {
                    throw new MaxRetryException(ERROR_MAX_RETRIES);
                }
            }
        }
    }
}
