package bakery.caker.dto;

import bakery.caker.config.Authority;
import bakery.caker.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUserDTO implements Serializable {
    private Long memberId;
    private Long kakaoId;
    private Authority authority;

    @Builder
    public SessionUserDTO(Member member) {
        this.kakaoId = member.getKakaoId();
        this.memberId = member.getMemberId();
        this.authority = member.getAuthority();
    }
}
