package uz.qirol.eombor.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "types")
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "parent")
    private Long parentId;

    @Column(name = "market")
    private Long marketId;
}
