package bakery.caker.service;

import bakery.caker.domain.Comment;
import bakery.caker.domain.Recomment;
import bakery.caker.dto.CommentDTO;
import bakery.caker.dto.CommentResponseDTO;
import bakery.caker.repository.CommentRepository;
import bakery.caker.repository.MemberRepository;
import bakery.caker.repository.OrderRepository;
import bakery.caker.repository.RecommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;

    //comment 작성
    @Transactional
    public void createComment(Long memberId, Long orderId, CommentDTO comment){
        memberRepository.findById(memberId).ifPresent(
                member -> orderRepository.findById(orderId).ifPresent(
                        order -> commentRepository.save(comment.toEntity(member, order))
                )
        );
    }

    //comment 삭제
    @Transactional
    public void deleteComment(Long memberId, Long commentId){
        memberRepository.findById(memberId).flatMap(member -> commentRepository.findById(commentId)).ifPresent(comment -> {
            if (memberId.equals(comment.getWriter().getMemberId())) {
                comment.updateDeletedFlag();
                commentRepository.save(comment);
            }
        });
    }

    //recomment 작성
    @Transactional
    public void createRecomment(Long memberId, Long commentId, CommentDTO recomment){
        memberRepository.findById(memberId).ifPresent(
                member -> commentRepository.findById(commentId).ifPresent(
                        comment -> recommentRepository.save(recomment.toRecommentEntity(member, comment))
                )
        );
    }

    //recomment 삭제
    @Transactional
    public void deleteRecomment(Long memberId, Long recommentId){
        memberRepository.findById(memberId).flatMap(member -> recommentRepository.findById(recommentId)).ifPresent(recomment -> {
            if (memberId.equals(recomment.getWriter().getMemberId())) {
                recomment.updateDeletedFlag();
                recommentRepository.save(recomment);
            }
        });
    }

    //order comment 불러오기
    public CommentResponseDTO getComments(Long orderId){
        Map<Comment, List<Recomment>> comments = new HashMap<>();
        orderRepository.findById(orderId).ifPresent(
                order -> {
                    List<Comment> commentList = commentRepository.findAllByOrder(order);
                    for(Comment comment : commentList){
                        comments.put(comment, recommentRepository.findAllByComment(comment));
                    }
                }
        );
        return new CommentResponseDTO(comments);
    }
}
