package uz.qirol.eombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.qirol.eombor.model.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByMarketId(Long marketId);
}
