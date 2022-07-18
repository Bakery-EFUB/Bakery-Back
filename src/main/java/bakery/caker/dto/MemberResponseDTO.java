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
    private String imageName;
    private String imageUrl;

    @Builder
    public MemberResponseDTO(Member entity, String imageUrl) {
        this.nickname = entity.getNickname();
        this.imageName = entity.getImage();
        this.imageUrl = imageUrl;
    }

    public static class MemberProfileResponseDTO {
        private String nickname;
        private String email;
        private String imageName;
        private String imageUrl;
        private Authority role;

        @Builder
        public MemberProfileResponseDTO(Member entity, String imageUrl) {
            this.nickname = entity.getNickname();
            this.email = entity.getEmail();
            this.imageName = entity.getImage();
            this.imageUrl = imageUrl;
            this.role = entity.getAuthority();
        }
    }
}
