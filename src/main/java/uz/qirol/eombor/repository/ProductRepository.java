package uz.qirol.eombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.qirol.eombor.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByMarketId(Long id);

    Optional<Product> findByName(String name);

    List<Product> findAllByMarketIdAndType1(Long mId, Long t1);
    List<Product> findAllByMarketIdAndType1AndType2(Long mId, Long t1, Long t2);



}
