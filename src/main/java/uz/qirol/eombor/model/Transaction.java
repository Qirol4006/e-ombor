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

    @Column(name = "market")
    private Long marketId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user")
    private String user;

    @Column(name = "soni")
    private Long soni;

    @Column(name = "sell_date")
    private LocalDateTime sellDate;
}
