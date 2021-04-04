package uz.qirol.eombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.qirol.eombor.model.Permission;

import java.util.Optional;

public interface PermissionRequest extends JpaRepository<Permission, Long> {

    Optional<Permission> findByUserIdAndAndMagazinId(Long userId, Long magazinId);
    Optional<Permission> findByUserId(Long userId);
}
