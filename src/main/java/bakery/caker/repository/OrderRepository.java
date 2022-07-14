package bakery.caker.repository;

import bakery.caker.domain.Member;
import bakery.caker.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByFinishedFlag(Boolean finishedFlag);
    List<Order> findAllByLocationGuAndLocationDongAndFinishedFlag(String locationGu, String locationDong, Boolean finishedFlag);
    List<Order> findAllByFinishedFlagOrderByCreatedAtDesc(Boolean finishedFlag);
    List<Order> findAllByMember(Member member);
}
