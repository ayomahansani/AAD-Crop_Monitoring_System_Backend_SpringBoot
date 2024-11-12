package lk.ijse.CropMonitoringSystem_Backend.service;

import lk.ijse.CropMonitoringSystem_Backend.dto.StaffStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.StaffDTO;

import java.util.List;

public interface StaffService {
    void saveStaff(StaffDTO staffDTO);

    StaffStatus getSelectedStaff(String staffId);

    List<StaffDTO> getAllStaffs();

    void deleteStaff(String staffId);

    void updateStaff(String staffId, StaffDTO updatedStaffDTO);
}
