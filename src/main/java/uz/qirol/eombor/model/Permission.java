package uz.qirol.eombor.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "permission_request")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mag_id")
    private Long magazinId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "permission")
    private String permission;

    @Column(name = "name")
    private String name;
}
