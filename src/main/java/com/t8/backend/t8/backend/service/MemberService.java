package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.MemberDto;
import com.t8.backend.t8.backend.entity.Member;
import com.t8.backend.t8.backend.repository.MemberRepository;
import com.t8.backend.t8.backend.security.entity.UserInfo;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

//    private Member toEntity(MemberDto dto) {
//        Member.MemberBuilder builder = Member.builder()
//                .id(dto.getId())
//                .name(dto.getName())
//                .email(dto.getEmail())
//                .phoneNumber(dto.getPhoneNumber())
//                .address(dto.getAddress())
//                .birthDate(dto.getBirthDate())
//                .status(dto.getStatus() != null ? dto.getStatus() : Member.Status.ACTIVE)
//                .maxReservationCount(dto.getMaxReservationCount() != null ? dto.getMaxReservationCount() : 0)
//                .noshowCounts(dto.getNoshowCounts() != null ? dto.getNoshowCounts() : 0);
//
//        return builder.build();
//    }

    private Member toEntity(MemberDto dto, UserInfo currentUser) {
        return Member.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .birthDate(dto.getBirthDate())
                .status(dto.getStatus() != null ? dto.getStatus() : Member.Status.ACTIVE)
                .maxReservationCount(dto.getMaxReservationCount() != null ? dto.getMaxReservationCount() : 0)
                .noshowCounts(dto.getNoshowCounts() != null ? dto.getNoshowCounts() : 0)
                .userInfo(currentUser)
                .build();
    }


//    private MemberDto toDto(Member member) {
//        MemberDto.MemberDtoBuilder builder = MemberDto.builder()
//                .id(member.getId())
//                .name(member.getName())
//                .email(member.getEmail())
//                .phoneNumber(member.getPhoneNumber())
//                .address(member.getAddress())
//                .birthDate(member.getBirthDate())
//                .status(member.getStatus())
//                .maxReservationCount(member.getMaxReservationCount())
//                .noshowCounts(member.getNoshowCounts())
//                .createdAt(member.getCreatedAt())
//                .updatedAt(member.getUpdatedAt());
//
//        return builder.build();
//    }

    private MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .address(member.getAddress())
                .birthDate(member.getBirthDate())
                .status(member.getStatus())
                .maxReservationCount(member.getMaxReservationCount())
                .noshowCounts(member.getNoshowCounts())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }



    @Transactional
    public MemberDto create(@Valid MemberDto dto, UserInfo currentUser) {
        Member member = toEntity(dto, currentUser);
        return toDto(memberRepository.save(member));
    }

    public MemberDto getById(Long id) {
        return memberRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id));
    }

    public List<MemberDto> getAll() {
        return memberRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberDto update(Long id, MemberDto dto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id));

        member.setName(dto.getName());
        member.setPhoneNumber(dto.getPhoneNumber());
        member.setAddress(dto.getAddress());
        member.setBirthDate(dto.getBirthDate());
//        member.setStatus(dto.getStatus());
//        member.setMaxReservationCount(dto.getMaxReservationCount());
//        member.setNoshowCounts(dto.getNoshowCounts());
        member.setStatus(dto.getStatus() != null ? dto.getStatus() : member.getStatus());
        member.setMaxReservationCount(dto.getMaxReservationCount() != null ? dto.getMaxReservationCount() : member.getMaxReservationCount());
        member.setNoshowCounts(dto.getNoshowCounts() != null ? dto.getNoshowCounts() : member.getNoshowCounts());

        return toDto(memberRepository.save(member));
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}
