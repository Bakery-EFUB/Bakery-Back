package bakery.caker.service;

import bakery.caker.domain.Member;
import bakery.caker.dto.OAuthAttributesDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class OAuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        OAuthAttributesDTO attributes = OAuthAttributesDTO.ofKakao(oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        httpSession.setAttribute("user",new SessionUserDTO(member));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                attributes.getAttributes(), "id");
    }

    private Member saveOrUpdate(OAuthAttributesDTO attributes){
        Member member = memberRepository.findMemberByKakaoId(attributes.getKakaoId());
        if(member!=null){
            return member;
        } else{
            memberRepository.save(attributes.toEntity());
            return memberRepository.findMemberByKakaoId(attributes.getKakaoId());
        }
    }
}
