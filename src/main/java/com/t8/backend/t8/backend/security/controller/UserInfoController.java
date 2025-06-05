package com.t8.backend.t8.backend.security.controller;

import com.t8.backend.t8.backend.dto.MemberDto;
import com.t8.backend.t8.backend.security.controller.dto.AuthRequest;
import com.t8.backend.t8.backend.security.controller.dto.SignUpRequestDto;
import com.t8.backend.t8.backend.security.entity.UserInfo;
import com.t8.backend.t8.backend.security.jwt.JwtService;
import com.t8.backend.t8.backend.service.MemberService;
import com.t8.backend.t8.backend.security.repository.UserInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userinfos")
public class UserInfoController {
    @Autowired
    private UserInfoRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MemberService memberService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

//    @PostMapping("/new")
//    public String addNewUser(@RequestBody UserInfo userInfo) {
//        userInfo.setPassword(
//                passwordEncoder.encode(userInfo.getPassword()));
//        UserInfo savedUserInfo = repository.save(userInfo);
//        return savedUserInfo.getName() + " user added!!";
//    }

    @PostMapping("/new")
    public ResponseEntity<MemberDto> signUp(@Valid @RequestBody SignUpRequestDto dto) {
        // 1. UserInfo 생성 및 저장
        UserInfo userInfo = new UserInfo();
        userInfo.setName(dto.getName());
        userInfo.setEmail(dto.getEmail());
        userInfo.setPassword(passwordEncoder.encode(dto.getPassword()));
//        userInfo.setPhoneNumber(dto.getPhoneNumber());
//        userInfo.setAddress(dto.getAddress());
//        userInfo.setBirthDate(dto.getBirthDate());
        userInfo.setRoles("ROLE_USER"); // 기본 권한 설정
        repository.save(userInfo);

        // 2. MemberDto 생성
        MemberDto memberDto = MemberDto.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .birthDate(dto.getBirthDate())
                .build();

        // 3. memberService 호출
        return ResponseEntity.ok(memberService.create(memberDto, userInfo));
    }


    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                ));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        try {
            String token = jwtService.extractToken(request);
            String email = jwtService.extractUsername(token); // subject에 email 저장되어 있음

            // (선택) RefreshToken 삭제 로직이 있다면 여기에 추가
            // refreshTokenRepository.deleteByEmail(email);

            return ResponseEntity.ok("로그아웃 완료");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰입니다.");
        }
    }
}