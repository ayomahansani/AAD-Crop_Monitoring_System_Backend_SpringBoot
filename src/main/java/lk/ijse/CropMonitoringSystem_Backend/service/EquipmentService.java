package lk.ijse.CropMonitoringSystem_Backend.service;

import lk.ijse.CropMonitoringSystem_Backend.dto.EquipmentStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.EquipmentDTO;

import java.util.List;

public interface EquipmentService {
    void saveEquipment(EquipmentDTO equipmentDTO);

    EquipmentStatus getSelectedEquipment(String equipmentId);

    List<EquipmentDTO> getAllEquipments();

    void deleteEquipment(String equipmentId);

    void updateEquipment(String equipmentId, EquipmentDTO updatedEquipmentDTO);
}
