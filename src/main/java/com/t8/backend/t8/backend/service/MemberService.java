package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.MemberDto;
import java.util.List;

public interface MemberService {
    MemberDto register(MemberDto dto);
    MemberDto getById(Long id);
    List<MemberDto> getAll();
    MemberDto update(Long id, MemberDto dto);
    void delete(Long id);
}