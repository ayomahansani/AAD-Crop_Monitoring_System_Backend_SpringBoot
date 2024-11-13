package lk.ijse.CropMonitoringSystem_Backend.service;

import lk.ijse.CropMonitoringSystem_Backend.dto.CropStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.CropDTO;

import java.util.List;

public interface CropService {
    void saveCrop(CropDTO cropDTO);

    CropStatus getSelectedCrop(String cropCode);

    List<CropDTO> getAllCrops();

    void deleteCrop(String cropCode);
}
