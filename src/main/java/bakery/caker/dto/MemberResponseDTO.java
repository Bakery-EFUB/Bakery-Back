package bakery.caker.dto;

import bakery.caker.config.Authority;
import bakery.caker.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDTO {
    private String nickname;
    private String email;
    private String imageName;
    private String imageUrl;
    private Authority role;

    @Builder
    public MemberResponseDTO(Member entity, String imageUrl) {
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.imageName = entity.getImage();
        this.role = entity.getAuthority();
        this.imageUrl = imageUrl;
    }
}
