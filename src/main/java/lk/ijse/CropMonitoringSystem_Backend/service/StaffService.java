package lk.ijse.CropMonitoringSystem_Backend.service;

import lk.ijse.CropMonitoringSystem_Backend.dto.StaffStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.FieldDTO;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.StaffDTO;

import java.util.List;
import java.util.Optional;

public interface StaffService {

    StaffDTO saveStaff(StaffDTO staffDTO);
    StaffStatus getSelectedStaff(String staffId);
    List<StaffDTO> getAllStaffs();
    void deleteStaff(String staffId);
    void updateStaff(String staffId, StaffDTO updatedStaffDTO);

    Optional<StaffDTO> findByEmail(String email);
    List<FieldDTO> getFieldsByStaffId(String staffId);
}