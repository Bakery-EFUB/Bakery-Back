package bakery.caker.dto;

import bakery.caker.domain.Comment;
import bakery.caker.domain.Recomment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class CommentResponseDTO {
    Map<Comment, List<Recomment>> comments;

    @Builder
    public CommentResponseDTO(Map<Comment, List<Recomment>> comments){
        this.comments = comments;
    }
}
