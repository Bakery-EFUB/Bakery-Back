package bakery.caker.domain;

import lombok.*;
import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "comment")
public class Comment extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String contents;
    private String nickname;
    private Boolean deletedFlag;

    @ManyToOne()
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne()
    @JoinColumn(name = "order_id")
    private Sheet sheet;

    @Builder
    public Comment(String contents, String nickname, Member writer, Sheet sheet){
        this.contents = contents;
        this.nickname = nickname;
        this.writer = writer;
        this.sheet = sheet;
        this.deletedFlag = false;
    }

    public void updateDeletedFlag(){
        this.deletedFlag = true;
    }
}
