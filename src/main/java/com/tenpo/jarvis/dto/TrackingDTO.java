package com.tenpo.jarvis.dto;

import com.tenpo.jarvis.entity.Tracking;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TrackingDTO {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private String httpMethod;

    @Getter
    @Setter
    private String statusCode;

    @Getter
    @Setter
    private byte[] response;

    @Getter
    @Setter
    private String error;

    @Getter
    @Setter
    private LocalDateTime createdAt;

    public static TrackingDTO parseEntity(final Tracking tracking){
        return TrackingDTO.builder().id(tracking.getId())
                .url(tracking.getUrl())
                .httpMethod(tracking.getHttpMethod())
                .statusCode(tracking.getStatusCode())
                .response(tracking.getResponse())
                .error(tracking.getError())
                .createdAt(tracking.getCreatedAt())
                .build();
    }
}
