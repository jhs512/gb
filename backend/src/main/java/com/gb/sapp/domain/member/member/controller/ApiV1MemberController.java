package com.gb.sapp.domain.member.member.controller;

import com.gb.sapp.domain.member.member.dto.MemberDto;
import com.gb.sapp.domain.member.member.service.MemberService;
import com.gb.sapp.global.rq.Rq;
import com.gb.sapp.global.rsData.RsData;
import com.gb.sapp.standard.base.Empty.Empty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MemberController {
    private final MemberService memberService;
    private final Rq rq;

    public record MemberLoginReqBody(@NotBlank String username, @NotBlank String password) {
    }

    public record MemberLoginResBody(@NonNull MemberDto item) {
    }

    @PostMapping(value = "/login")
    public RsData<MemberLoginResBody> login(@Valid @RequestBody MemberLoginReqBody body) {
        RsData<MemberService.MemberAuthAndMakeTokensResBody> authAndMakeTokensRs = memberService.authAndMakeTokens(
                body.username,
                body.password
        );

        rq.setCrossDomainCookie("refreshToken", authAndMakeTokensRs.getData().refreshToken());
        rq.setCrossDomainCookie("accessToken", authAndMakeTokensRs.getData().accessToken());

        return authAndMakeTokensRs.newDataOf(
                new MemberLoginResBody(
                        new MemberDto(authAndMakeTokensRs.getData().member())
                )
        );
    }

    public record MeResponseBody(@NonNull MemberDto item) {
    }

    @GetMapping("/me")
    public RsData<MeResponseBody> getMe() {
        return RsData.of(
                new MeResponseBody(
                        new MemberDto(rq.getMember())
                )
        );
    }


    @PostMapping("/logout")
    public RsData<Empty> logout() {
        rq.setLogout();

        return RsData.of("로그아웃 성공");
    }
}
