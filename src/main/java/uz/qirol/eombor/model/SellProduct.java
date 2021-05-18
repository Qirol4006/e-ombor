package uz.qirol.eombor.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sold_products")
public class SellProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "market_id")
    private Long marketId;

    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "price")
    private Long price;

    @Column(name = "count")
    private Long count;
}
