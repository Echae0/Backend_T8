package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.MemberDto;
import com.t8.backend.t8.backend.entity.Member;
import com.t8.backend.t8.backend.repository.MemberRepository;
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
//        return Member.builder()
//                .id(dto.getId())
////                .memberNumber(dto.getMemberNumber())
//                .name(dto.getName())
//                .email(dto.getEmail())
//                .phoneNumber(dto.getPhoneNumber())
//                .address(dto.getAddress())
//                .birthDate(dto.getBirthDate())
//                .status(dto.getStatus())
//                .maxReservationCount(dto.getMaxReservationCount())
//                .noshowCounts(dto.getNoshowCounts())
//                .build();
//    }
    private Member toEntity(MemberDto dto) {
        Member.MemberBuilder builder = Member.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .birthDate(dto.getBirthDate())
                .status(dto.getStatus() != null ? dto.getStatus() : Member.Status.ACTIVE)
                .maxReservationCount(dto.getMaxReservationCount() != null ? dto.getMaxReservationCount() : 0)
                .noshowCounts(dto.getNoshowCounts() != null ? dto.getNoshowCounts() : 0);

        return builder.build();
    }


    private MemberDto toDto(Member member) {
        MemberDto.MemberDtoBuilder builder = MemberDto.builder()
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
                .updatedAt(member.getUpdatedAt());

        return builder.build();
    }



    @Transactional
    public MemberDto create(MemberDto dto) {
        Member member = toEntity(dto);
        return toDto(memberRepository.save(member));
    }

    public MemberDto getById(Long id) {
        return memberRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id));
    }

    public List<MemberDto> getAll() {
        return memberRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public MemberDto update(Long id, MemberDto dto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + id));

        member.setName(dto.getName());
        member.setPhoneNumber(dto.getPhoneNumber());
        member.setAddress(dto.getAddress());
        member.setBirthDate(dto.getBirthDate());
        member.setStatus(dto.getStatus());
        member.setMaxReservationCount(dto.getMaxReservationCount());
        member.setNoshowCounts(dto.getNoshowCounts());

        return toDto(memberRepository.save(member));
    }

    @Transactional
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}
