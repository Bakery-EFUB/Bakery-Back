package bakery.caker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDTO {
    private String nickname;
    private String email;

    @Builder
    public MemberRequestDTO(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
