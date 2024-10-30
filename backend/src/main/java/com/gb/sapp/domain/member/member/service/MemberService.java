package com.gb.sapp.domain.member.member.service;

import com.gb.sapp.domain.member.member.entity.Member;
import com.gb.sapp.domain.member.member.repository.MemberRepository;
import com.gb.sapp.global.rsData.RsData;
import com.gb.sapp.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AuthTokenService authTokenService;

    @Transactional
    public Member join(String username, String password, String nickname) {
        findByUsername(username).ifPresent(member -> {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        });

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .refreshToken(authTokenService.genRefreshToken())
                .build();

        memberRepository.save(member);

        return member;
    }

    public long count() {
        return memberRepository.count();
    }

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public record MemberAuthAndMakeTokensResBody(
            Member member,
            String accessToken,
            String refreshToken
    ) {
    }

    @Transactional
    public RsData<MemberAuthAndMakeTokensResBody> authAndMakeTokens(String username, String password) {
        Member member = findByUsername(username).get();

        if (!passwordMatches(member, password))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        String refreshToken = member.getRefreshToken();
        String accessToken = authTokenService.genAccessToken(member);

        return RsData.of(
                "%s님 안녕하세요.".formatted(member.getUsername()),
                new MemberAuthAndMakeTokensResBody(member, accessToken, refreshToken)
        );
    }

    @Transactional
    public String genAccessToken(Member member) {
        return authTokenService.genAccessToken(member);
    }

    public boolean passwordMatches(Member member, String password) {
        return passwordEncoder.matches(password, member.getPassword());
    }

    public SecurityUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = authTokenService.getDataFrom(accessToken);

        long id = (int) payloadBody.get("id");
        String username = (String) payloadBody.get("username");
        List<String> authorities = (List<String>) payloadBody.get("authorities");

        return new SecurityUser(
                id,
                username,
                "",
                authorities.stream().map(SimpleGrantedAuthority::new).toList()
        );
    }

    public boolean validateToken(String token) {
        return authTokenService.validateToken(token);
    }

    public RsData<String> refreshAccessToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken).get();

        String accessToken = authTokenService.genAccessToken(member);

        return RsData.of("200-1", "토큰 갱신 성공", accessToken);
    }
}
