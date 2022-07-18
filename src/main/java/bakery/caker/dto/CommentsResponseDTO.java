package bakery.caker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommentsResponseDTO {
    private List<CommentResponseDTO> commentDTO;

    @Builder
    public CommentsResponseDTO (List<CommentResponseDTO> commentDTO){
        this.commentDTO = commentDTO;
    }
}
