package bakery.caker.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String contents;
    private String nickname;
    private LocalDateTime createdAt;

    @ManyToOne()
    @JoinColumn(name = "member_id")
    private Member writer;

    @ManyToOne()
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public Comment(String contents, String nickname, Member writer, Order order){
        this.contents = contents;
        this.nickname = nickname;
        this.createdAt = LocalDateTime.now();
        this.writer = writer;
        this.order = order;
    }
}
