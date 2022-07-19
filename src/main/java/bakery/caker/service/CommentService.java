package bakery.caker.service;

import bakery.caker.domain.Comment;
import bakery.caker.dto.CommentDTO;
import bakery.caker.dto.CommentResponseDTO;
import bakery.caker.dto.CommentsResponseDTO;
import bakery.caker.repository.CommentRepository;
import bakery.caker.repository.MemberRepository;
import bakery.caker.repository.SheetRepository;
import bakery.caker.repository.RecommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final MemberRepository memberRepository;
    private final SheetRepository sheetRepository;
    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;

    //comment 작성
    @Transactional
    public void addComment(Long memberId, Long orderId, CommentDTO comment){
        memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId).ifPresent(
                member -> sheetRepository.findById(orderId).ifPresent(
                        order -> commentRepository.save(comment.toEntity(member, order))
                )
        );
    }

    //comment 삭제
    @Transactional
    public void removeComment(Long memberId, Long commentId){
        memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId).flatMap(member -> commentRepository.findById(commentId)).ifPresent(comment -> {
            if (memberId.equals(comment.getWriter().getMemberId())) {
                comment.updateDeletedFlag();
                commentRepository.save(comment);
            }
        });
    }

    //recomment 작성
    @Transactional
    public void addRecomment(Long memberId, Long commentId, CommentDTO recomment){
        memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId).ifPresent(
                member -> commentRepository.findById(commentId).ifPresent(
                        comment -> recommentRepository.save(recomment.toRecommentEntity(member, comment))
                )
        );
    }

    //recomment 삭제
    @Transactional
    public void removeRecomment(Long memberId, Long recommentId){
        memberRepository.findMemberByMemberIdAndDeleteFlagIsFalse(memberId).flatMap(member -> recommentRepository.findById(recommentId)).ifPresent(recomment -> {
            if (memberId.equals(recomment.getWriter().getMemberId())) {
                recomment.updateDeletedFlag();
                recommentRepository.save(recomment);
            }
        });
    }

    //order comment 불러오기
    public CommentsResponseDTO findComments(Long orderId){
        List<CommentResponseDTO> comments = new ArrayList<>();
        sheetRepository.findById(orderId).ifPresent(
                order -> {
                    List<Comment> commentList = commentRepository.findAllBySheet(order);
                    for(Comment comment : commentList){
                        comments.add(new CommentResponseDTO(comment, recommentRepository.findAllByComment(comment)));
                    }
                }
        );
        return new CommentsResponseDTO(comments);
    }
}
