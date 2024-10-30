package com.gb.sapp.domain.member.member.controller;

import com.gb.sapp.domain.member.member.dto.MemberDto;
import com.gb.sapp.domain.member.member.service.MemberService;
import com.gb.sapp.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class ApiV1MemberController {
    private final MemberService memberService;

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

        return authAndMakeTokensRs.newDataOf(
                new MemberLoginResBody(
                        new MemberDto(authAndMakeTokensRs.getData().member())
                )
        );
    }
}
