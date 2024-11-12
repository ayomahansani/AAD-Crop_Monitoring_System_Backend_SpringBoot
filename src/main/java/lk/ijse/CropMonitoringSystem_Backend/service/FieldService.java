package lk.ijse.CropMonitoringSystem_Backend.service;

import lk.ijse.CropMonitoringSystem_Backend.dto.FieldStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.FieldDTO;

import java.util.List;

public interface FieldService {
    void saveField(FieldDTO fieldDTO);

    FieldStatus getSelectedField(String fieldCode);

    List<FieldDTO> getAllFields();

    void deleteField(String fieldCode);

    void updateField(String fieldCode, FieldDTO updatedFieldDTO);
}
