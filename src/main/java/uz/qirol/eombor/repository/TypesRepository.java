package uz.qirol.eombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.qirol.eombor.model.ProductType;

import java.util.List;
import java.util.Optional;

public interface TypesRepository extends JpaRepository<ProductType, Long> {

    List<ProductType> findAllByMarketId(Long id);
    List<ProductType> findAllByMarketIdAndParentId(Long marketId, Long parentId);
}
