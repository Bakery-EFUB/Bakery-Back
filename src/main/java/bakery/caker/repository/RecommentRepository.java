package bakery.caker.repository;

import bakery.caker.domain.Comment;
import bakery.caker.domain.Member;
import bakery.caker.domain.Recomment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommentRepository extends JpaRepository<Recomment, Long> {
    List<Recomment> findAllByWriter(Member writer);
    List<Recomment> findAllByComment(Comment comment);
}
