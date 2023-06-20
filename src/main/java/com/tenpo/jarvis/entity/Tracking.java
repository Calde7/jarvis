package com.tenpo.jarvis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tracking")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "url")
    @Getter
    @Setter
    private String url;

    @Column(name = "http_method")
    @Getter
    @Setter
    private String httpMethod;

    @Column(name = "status_code")
    @Getter
    @Setter
    private String statusCode;

    @Column(name = "response")
    @Getter
    @Setter
    private byte[] response;

    @Column(name = "error")
    @Getter
    @Setter
    private String error;

    @Column(name = "created_at")
    @Getter
    @Setter
    private LocalDateTime createdAt;


}
