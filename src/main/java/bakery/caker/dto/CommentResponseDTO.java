package bakery.caker.dto;

import bakery.caker.domain.Comment;
import bakery.caker.domain.Recomment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommentResponseDTO {
    private Comment comment;
    private List<Recomment> recomments;

    @Builder
    public CommentResponseDTO(Comment comment, List<Recomment> recomments){
        this.comment = comment;
        this.recomments = recomments;
    }
}
