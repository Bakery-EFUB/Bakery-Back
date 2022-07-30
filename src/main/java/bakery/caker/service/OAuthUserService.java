package bakery.caker.service;

import bakery.caker.config.Authority;
import bakery.caker.domain.Member;
import bakery.caker.dto.OAuthAttributesDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.exception.CustomException;
import bakery.caker.exception.ErrorCode;
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
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OAuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        try{
            OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            OAuthAttributesDTO attributes = OAuthAttributesDTO.ofKakao(oAuth2User.getAttributes());

            Map<String, Object> newAttribute = updateAttributes(attributes);
            SessionUserDTO sessionUser = saveOrUpdate(attributes);
            String key = sessionUser.getAuthority();


            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority(key)),
                    newAttribute, "id");

        }catch(OAuth2AuthenticationException e) {
            throw new CustomException(ErrorCode.EXCEPTION, e.getStackTrace().toString());
        }
    }

    private Map<String, Object> updateAttributes(OAuthAttributesDTO attributes) {
        Member member = memberRepository.findMemberByKakaoId(attributes.getKakaoId());
        Map<String, Object> newAttribute = new HashMap<String, Object>();
        newAttribute.putAll(attributes.getAttributes());

        if(member==null) {
            newAttribute.put("firstLogin", true);
        }
        else {
            newAttribute.put("firstLogin", false);
        }
        return newAttribute;
    }

    private SessionUserDTO saveOrUpdate(OAuthAttributesDTO attributes){
        Member member = memberRepository.findMemberByKakaoId(attributes.getKakaoId());
        if(member!=null){
            return new SessionUserDTO(member);
        } else{
            memberRepository.save(attributes.toEntity());
            Member newMember = memberRepository.findMemberByKakaoId(attributes.getKakaoId());
            return new SessionUserDTO(newMember);
        }
    }
}
