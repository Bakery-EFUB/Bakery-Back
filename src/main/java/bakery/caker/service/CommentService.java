package bakery.caker.service;

import bakery.caker.domain.Comment;
import bakery.caker.domain.Recomment;
import bakery.caker.dto.CommentDTO;
import bakery.caker.dto.CommentResponseDTO;
import bakery.caker.dto.CommentsResponseDTO;
import bakery.caker.dto.SessionUserDTO;
import bakery.caker.exception.CustomException;
import bakery.caker.exception.ErrorCode;
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
                List<Recomment> recomments = recommentRepository.findAllByCommentAndDeletedFlagFalse(comment);
                recomments.forEach(Recomment::updateDeletedFlag);
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
                    List<Comment> commentList = commentRepository.findAllBySheetAndDeletedFlagFalse(order);
                    for(Comment comment : commentList){
                        comments.add(new CommentResponseDTO(comment, recommentRepository.findAllByCommentAndDeletedFlagFalse(comment)));
                    }
                }
        );
        return new CommentsResponseDTO(comments);
    }

    public void commentWriterCheck(SessionUserDTO sessionUser, Long commentId) {
        if(!sessionUser.getMemberId().equals(findComment(commentId).getWriter().getMemberId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "작성자 본인이 아닙니다.");
        }
    }

    public void recommentWriterCheck(SessionUserDTO sessionUser, Long recommentId) {
        if(!sessionUser.getMemberId().equals(findRecomment(recommentId).getWriter().getMemberId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED, "작성자 본인이 아닙니다.");
        }
    }

    public Comment findComment(Long commentId){
        Comment comment = commentRepository.findByCommentIdAndDeletedFlagFalse(commentId).orElseThrow(()
                -> new CustomException(ErrorCode.COMMENT_NOT_FOUND, "삭제되거나 없는 댓글입니다. id= " + commentId));
        return comment;
    }

    public Recomment findRecomment(Long recommentId){
        Recomment recomment = recommentRepository.findByRecommentIdAndDeletedFlagFalse(recommentId).orElseThrow(()
                -> new CustomException(ErrorCode.COMMENT_NOT_FOUND, "삭제되거나 없는 댓글입니다. id= " + recommentId));
        return recomment;
    }
}
