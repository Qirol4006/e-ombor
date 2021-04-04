package uz.qirol.eombor.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "accepted")
public class ReqAccepted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "market_id")
    private Long marketId;
}
