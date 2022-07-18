package bakery.caker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CommentsResponseDTO {
    private List<CommentResponseDTO> commentDTOs;

    @Builder
    public CommentsResponseDTO (List<CommentResponseDTO> commentDTO){
        this.commentDTOs = commentDTO;
    }
}
