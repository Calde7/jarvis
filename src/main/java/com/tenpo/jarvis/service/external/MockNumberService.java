package com.tenpo.jarvis.service.external;

import com.tenpo.jarvis.dto.RandomNumberDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
@Scope(value = "singleton")
public class MockNumberService {
    private final String mockUrl = "https://csrng.net/csrng/csrng.php?min=0&max=100";

    public BigDecimal getRandomNumber() {
        RestTemplate restTemplate = new RestTemplate();

        final ResponseEntity<RandomNumberDTO[]> response = restTemplate.getForEntity(mockUrl, RandomNumberDTO[].class);

        return new BigDecimal(response.getBody()[0].getRandom());
    }
}
