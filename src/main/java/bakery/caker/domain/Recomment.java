package bakery.caker.domain;

import com.sun.istack.NotNull;
import lombok.*;
import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "recomment")
public class Recomment extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommentId;

    @NotNull
    private String contents;
    @NotNull
    private String nickname;
    private Boolean deletedFlag;

    @ManyToOne()
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne()
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public Recomment(String contents, String nickname, Member writer, Comment comment){
        this.contents = contents;
        this.nickname = nickname;
        this.writer = writer;
        this.comment = comment;
        this.deletedFlag = false;
    }

    public void updateDeletedFlag(){
        this.deletedFlag = true;
    }
}
