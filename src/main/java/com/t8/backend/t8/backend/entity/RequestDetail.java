package com.t8.backend.t8.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "request_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDetail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    private String requestType;
    private String content;
    private Boolean isActive;
}