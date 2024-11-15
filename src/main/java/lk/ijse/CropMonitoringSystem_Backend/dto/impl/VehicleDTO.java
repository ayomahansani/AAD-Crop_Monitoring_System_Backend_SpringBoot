package lk.ijse.CropMonitoringSystem_Backend.dto.impl;

import lk.ijse.CropMonitoringSystem_Backend.dto.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class VehicleDTO implements VehicleStatus {

    private String vehicleCode;
    private String licensePlateNumber;
    private String vehicleCategory;
    private String fuelType;
    private String vehicleStatus;
    private String remarks;
    private String staffId;
}
