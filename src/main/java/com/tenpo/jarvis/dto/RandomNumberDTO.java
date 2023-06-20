package com.tenpo.jarvis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
public class RandomNumberDTO {

    @Getter
    @Setter
    @JsonProperty("status")
    private String status;

    @Getter
    @Setter
    @JsonProperty("min")
    private Integer min;

    @Getter
    @Setter
    @JsonProperty("max")
    private Integer max;

    @Getter
    @Setter
    @JsonProperty("random")
    private Integer random;
}
