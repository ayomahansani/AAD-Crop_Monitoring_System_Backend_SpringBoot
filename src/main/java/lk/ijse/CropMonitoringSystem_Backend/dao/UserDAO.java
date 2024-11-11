package lk.ijse.CropMonitoringSystem_Backend.dao;

import lk.ijse.CropMonitoringSystem_Backend.entity.impl.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<UserEntity,String> {
}
