package uz.qirol.eombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.qirol.eombor.model.SellProduct;

import java.util.List;


public interface SellProds extends JpaRepository<SellProduct, Long> {

    List<SellProduct> findAllByMarketId(Long marketId);
    List<SellProduct> findAllByTransactionId(Long id);

}
