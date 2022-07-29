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
    private String imageUrl;

    @Builder
    public MemberResponseDTO(Member entity, String imageUrl) {
        this.nickname = entity.getNickname();
        this.imageUrl = imageUrl;
    }

    @Getter
    @NoArgsConstructor
    public static class MemberProfileResponseDTO {
        private String nickname;
        private String email;
        private String imageUrl;
        private String role;

        @Builder
        public MemberProfileResponseDTO(Member entity, String imageUrl) {
            this.nickname = entity.getNickname();
            this.email = entity.getEmail();
            this.imageUrl = imageUrl;
            this.role = entity.getAuthority();
        }
    }
}
