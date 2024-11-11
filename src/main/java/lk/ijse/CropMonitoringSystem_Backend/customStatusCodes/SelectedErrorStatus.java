package lk.ijse.CropMonitoringSystem_Backend.customStatusCodes;

import lk.ijse.CropMonitoringSystem_Backend.dto.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SelectedErrorStatus implements CropStatus, EquipmentStatus, FieldStatus, MonitoringLogStatus, StaffStatus, UserStatus, VehicleStatus {
    private int statusCode;
    private String statusMessage;
}
