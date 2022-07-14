package bakery.caker.dto;

import bakery.caker.config.Authority;
import bakery.caker.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributesDTO {
    private Map<String, Object> attributes;
    private Long kakaoId;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributesDTO(Map<String, Object> attributes, Long kakaoId, String name,
                              String email, String picture) {
        this.kakaoId = kakaoId;
        this.attributes = attributes;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributesDTO ofKakao(Map<String, Object> attributes){
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");

        return OAuthAttributesDTO.builder()
                .kakaoId((Long)attributes.get("id"))
                .name((String)profile.get("nickname"))
                .email((String)response.get("email"))
                .picture((String)profile.get("profile_image_url"))
                .attributes(attributes)
                .build();
    }

    public Member toEntity() {
        Member member = Member.builder()
                .kakaoId(kakaoId)
                .nickname(name)
                .image(picture)
                .email(email)
                .authority(Authority.CLIENT)
                .build();

        return member;
    }
}
