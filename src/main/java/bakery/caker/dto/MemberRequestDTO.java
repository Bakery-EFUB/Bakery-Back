package bakery.caker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDTO {
    private String nickname;
    private String name;
    private String phoneNum;

    @Builder
    public MemberRequestDTO(String nickname, String name, String phoneNum) {
        if(nickname!=null) this.nickname = nickname;
        if(name!=null) this.name = name;
        if(phoneNum!=null) this.phoneNum = phoneNum;
    }
}
