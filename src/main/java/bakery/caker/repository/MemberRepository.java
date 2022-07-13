package bakery.caker.repository;

import bakery.caker.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findMembersByMemberIdAndAndDeleteFlagIsFalse(Long memberId);
    Optional<Member> findMemberByMemberIdAndDeleteFlagIsFalse(Long memberId);
    Member findMemberByKakaoId(Long kakaoId);
}
