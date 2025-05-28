package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.ReservationDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReservationService {
    ReservationDto create(ReservationDto dto);
    ReservationDto getById(Long id);
    List<ReservationDto> getAll();
    ReservationDto updateStatus(Long id, String status);
    void cancel(Long id);
}