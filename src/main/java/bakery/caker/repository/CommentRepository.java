package bakery.caker.repository;

import bakery.caker.domain.Comment;
import bakery.caker.domain.Member;
import bakery.caker.domain.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByWriter(Member writer);
    List<Comment> findAllBySheet(Sheet sheet);
}
