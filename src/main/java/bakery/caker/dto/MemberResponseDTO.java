package bakery.caker.dto;

import bakery.caker.config.Authority;
import bakery.caker.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDTO {
    private String nickname;
    private String email;
    private String image;
    private Authority role;

    public MemberResponseDTO(Member entity) {
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.image = entity.getImage();
        this.role = entity.getAuthority();
    }
}
