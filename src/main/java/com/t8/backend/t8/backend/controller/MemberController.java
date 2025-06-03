package com.t8.backend.t8.backend.controller;

import com.t8.backend.t8.backend.dto.MemberDto; // 기존 MemberDto 사용
import com.t8.backend.t8.backend.service.MemberService; // MemberService 클래스를 임포트
import lombok.RequiredArgsConstructor; // Lombok 어노테이션 추가
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid; // 유효성 검증을 위해 추가
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor // final 필드(memberService)를 인자로 받는 생성자 자동 생성
public class MemberController {
    private final MemberService memberService; // 필드 이름을 memberService로 변경 (가독성)


    @PostMapping
    public ResponseEntity<MemberDto> create(@Valid @RequestBody MemberDto dto) {
        return ResponseEntity.ok(memberService.create(dto));
    }


    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<MemberDto>> list() { // MemberDto 리스트 반환
        return ResponseEntity.ok(memberService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<MemberDto> get(@PathVariable Long id) { // MemberDto 반환
        return ResponseEntity.ok(memberService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> update(@PathVariable Long id, @Valid @RequestBody MemberDto dto) { // MemberDto 그대로 사용
        return ResponseEntity.ok(memberService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content 응답
    }
}