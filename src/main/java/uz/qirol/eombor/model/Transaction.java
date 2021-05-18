package uz.qirol.eombor.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "market_id")
    private Long marketId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String user;

    @Column(name = "sell_date")
    private LocalDateTime sellDate;

    @Column(name = "summ")
    private Long soni;
}
