package bakery.caker.repository;

import bakery.caker.domain.Comment;
import bakery.caker.domain.Member;
import bakery.caker.domain.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByWriter(Member writer);
    List<Comment> findAllBySheet(Sheet sheet);
    Optional<Comment> findByCommentIdAndDeletedFlagFalse(Long commentId);
    Optional<Comment> findCommentsBySheetAndDeletedFlagFalse(Sheet sheet);
}
