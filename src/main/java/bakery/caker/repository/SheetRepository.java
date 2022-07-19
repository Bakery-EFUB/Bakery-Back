package bakery.caker.repository;

import bakery.caker.domain.Member;
import bakery.caker.domain.Sheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SheetRepository extends JpaRepository<Sheet, Long> {
    List<Sheet> findAllByFinishedFlag(Boolean finishedFlag);
    List<Sheet> findAllByLocationGuAndLocationDongAndFinishedFlag(String locationGu, String locationDong, Boolean finishedFlag);
    List<Sheet> findTop6ByFinishedFlagOrderByCreatedAtDesc(Boolean finishedFlag);
    List<Sheet> findAllByMember(Member member);
}
