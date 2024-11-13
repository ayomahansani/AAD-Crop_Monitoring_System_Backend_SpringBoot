package lk.ijse.CropMonitoringSystem_Backend.service;

import lk.ijse.CropMonitoringSystem_Backend.dto.VehicleStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.VehicleDTO;

import java.util.List;

public interface VehicleService {
    void saveVehicle(VehicleDTO vehicleDTO);
    VehicleStatus getSelectedVehicle(String vehicleCode);
    List<VehicleDTO> getAllVehicles();
    void deleteVehicle(String vehicleCode);
    void updateVehicle(String vehicleCode, VehicleDTO updatedVehicleDTO);
}
