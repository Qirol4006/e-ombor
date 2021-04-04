package uz.qirol.eombor.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type1")
    private Long type1;

    @Column(name = "type2")
    private Long type2;

    @Column(name = "real_price")
    private Long realPrice;

    @Column(name = "sell_price")
    private Long sellPrice;

    @Column(name = "soni")
    private Long soni;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Column(name = "market_id")
    private Long marketId;

    @Column(name = "valyuta")
    private String valyuta;
}
