package com.t8.backend.t8.backend.controller;

import com.t8.backend.t8.backend.dto.MemberDto;
import com.t8.backend.t8.backend.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService service;
    public MemberController(MemberService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<MemberDto> create(@RequestBody MemberDto dto) {
        return ResponseEntity.ok(service.register(dto));
    }

    @GetMapping
    public ResponseEntity<List<MemberDto>> list() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> update(@PathVariable Long id, @RequestBody MemberDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}