package bakery.caker.repository;
import bakery.caker.domain.Member;
import bakery.caker.dto.StoreResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bakery.caker.domain.Store;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findStoreByOwner(Member member);
    List<Store> findByNameContaining(String q);
    List<Store> findStoresByOwner_DeleteFlag(Boolean deleteFlag);
}