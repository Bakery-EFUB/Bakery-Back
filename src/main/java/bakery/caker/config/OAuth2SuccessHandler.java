package bakery.caker.config;

import bakery.caker.domain.Member;
import bakery.caker.repository.MemberRepository;
import bakery.caker.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
        Member member = memberRepository.findMemberByKakaoId(oAuth2User.getAttribute("id"));
        String role = member.getAuthority();
        Boolean firstLogin = oAuth2User.getAttribute("firstLogin");

        log.info("Principal에서 꺼낸 OAuth2User = {}", oAuth2User);
        String targetUrl;
        log.info("토큰 발행 시작");

        String token = jwtTokenProvider.createJwtAccessToken(oAuth2User.getAttribute("id").toString(), role);
        log.info("{}", token);
        targetUrl = UriComponentsBuilder.fromUriString("https://caker.shop/kakaologin")
                .queryParam("token", token)
                .queryParam("firstLogin", firstLogin)
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
