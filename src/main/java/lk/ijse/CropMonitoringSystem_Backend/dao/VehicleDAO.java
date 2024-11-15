package lk.ijse.CropMonitoringSystem_Backend.dao;

import lk.ijse.CropMonitoringSystem_Backend.entity.impl.StaffEntity;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleDAO extends JpaRepository<VehicleEntity,String> {
    List<VehicleEntity> findByStaff(StaffEntity staff);
}
