package uz.qirol.eombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.qirol.eombor.model.ProductSeller;

import java.util.List;

public interface SellerRepository extends JpaRepository<ProductSeller, Long> {
    List<ProductSeller> findAllByMarketId(Long marketId);
    List<ProductSeller> findAllByMarketIdAndType1(Long marketId, Long type1);
    List<ProductSeller> findAllByMarketIdAndType1AndAndType2(Long marketId, Long type1, Long type2);
}
