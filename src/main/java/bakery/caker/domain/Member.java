package bakery.caker.domain;

import bakery.caker.config.Authority;
import bakery.caker.dto.MemberRequestDTO;
import bakery.caker.dto.MemberResponseDTO;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column
    @NotNull
    private Long kakaoId;

    @Column
    @NotNull
    private String nickname;

    @Column
    private String name;

    @Column
    private String phoneNum;

    @Column
    private String email;

    @Column
    private String image;

    @Column(name = "role")
    @NotNull
    private String authority;

    @Column
    @NotNull
    private Boolean deleteFlag;

    @Builder
    public Member(Long kakaoId, String nickname, String email, String image, Authority authority) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        if(email!=null) this.email = email;
        this.image = image;
        this.authority = authority.getValue();
        this.deleteFlag = false;
    }

    public void updateAuthority(Authority authority) {
        this.authority = authority.getValue();
    }

    public void updateProfile(String nickname) {
        this.nickname = nickname;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void updateImage(String fileurl) {
        this.image = fileurl;
    }

    public void updateDeleteFlag() {
        this.deleteFlag = true;
    }
}
