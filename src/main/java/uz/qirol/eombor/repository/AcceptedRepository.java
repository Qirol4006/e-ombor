package uz.qirol.eombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.qirol.eombor.model.ReqAccepted;

import java.util.List;
import java.util.Optional;

public interface AcceptedRepository extends JpaRepository<ReqAccepted, Long> {
    Optional<ReqAccepted> findByUserIdAndMarketId(Long uId, Long mId);
    Optional<ReqAccepted> findByUserId(Long id);
    List<ReqAccepted> findAllByMarketId(Long marketId);
}
