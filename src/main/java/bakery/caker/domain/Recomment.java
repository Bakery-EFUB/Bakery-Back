package bakery.caker.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "recomment")
public class Recomment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommentId;

    private String contents;
    private String nickname;
    private LocalDateTime createdAt;
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
        this.createdAt = LocalDateTime.now();
        this.writer = writer;
        this.comment = comment;
    }

    public void updateDeletedFlag(){
        this.deletedFlag = true;
    }
}
