package bakery.caker.dto;

import bakery.caker.domain.Comment;
import bakery.caker.domain.Member;
import bakery.caker.domain.Order;
import bakery.caker.domain.Recomment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentDTO {
    private String contents;

    @Builder
    public CommentDTO(String contents){
        this.contents = contents;
    }

    public Comment toEntity(Member writer, Order order){
        return Comment.builder()
                .contents(contents)
                .nickname(writer.getNickname())
                .order(order)
                .writer(writer)
                .build();
    }

    public Recomment toRecommentEntity(Member writer, Comment comment){
        return Recomment.builder()
                .comment(comment)
                .nickname(writer.getNickname())
                .writer(writer)
                .contents(contents)
                .build();
    }
}
