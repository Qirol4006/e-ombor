package uz.qirol.eombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.qirol.eombor.model.Market;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long> {

    List<Market> findByName(String name);
}
