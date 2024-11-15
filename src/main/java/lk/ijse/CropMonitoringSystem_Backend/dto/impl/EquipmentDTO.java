package lk.ijse.CropMonitoringSystem_Backend.dto.impl;

import lk.ijse.CropMonitoringSystem_Backend.dto.EquipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class EquipmentDTO implements EquipmentStatus {

    private String equipmentId;
    private String equipmentName;
    private String equipmentType;
    private String equipmentStatus;
    private String fieldCode;
    private String staffId;
}
