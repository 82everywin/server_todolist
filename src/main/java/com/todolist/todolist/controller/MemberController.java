package com.todolist.todolist.controller;


import com.todolist.todolist.config.JwtTokenFilter;
import com.todolist.todolist.dto.member.MemberLoginResponseDto;
import com.todolist.todolist.dto.member.MemberResponseDto;
import com.todolist.todolist.dto.member.MemberRequestDto;
import com.todolist.todolist.security.CustomUserDetailService;
import com.todolist.todolist.security.JwtTokenProvider;
import com.todolist.todolist.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/members")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;

    private final CustomUserDetailService userDetailService;

    private final AuthenticationManagerBuilder authenticationManager;

    @Operation(summary = "회원가입")
    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestBody MemberRequestDto request) {

        MemberResponseDto responseDto = memberService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<String> loginMember(@RequestBody @Valid MemberRequestDto.LoginRequestDto request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword());
        Authentication authentication = authenticationManager.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.createToken(authentication);

        // 헤더에 담아 리턴
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtTokenFilter.AUTHORIZATION_HEADER, "Bearer " + token);

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(token);
    }
    @Operation(summary = "회원정보 수정")
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> updateMember(@RequestBody @Valid MemberRequestDto request, @PathVariable Long memberId){

        MemberResponseDto responseDto = memberService.update(memberId, request);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "Id별 회원 조회")
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> readOneMember(@PathVariable Long memberId){
        MemberResponseDto responseDto = memberService.searchId(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "일괄 회원 조회")
    @GetMapping
    public  ResponseEntity<List<MemberResponseDto>> readAllMember(){
        List<MemberResponseDto> responseDto = memberService.searchAll();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @Operation(summary = "회원 삭제")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> delete(@PathVariable Long memberId){
        memberService.delete(memberId);
        return ResponseEntity.noContent().build();
    }

}