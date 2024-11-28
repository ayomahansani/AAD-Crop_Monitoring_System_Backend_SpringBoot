package lk.ijse.CropMonitoringSystem_Backend.entity.impl;

import jakarta.persistence.*;
import lk.ijse.CropMonitoringSystem_Backend.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "vehicle")
public class VehicleEntity implements SuperEntity {

    @Id
    private String vehicleCode;
    private String licensePlateNumber;
    private String vehicleCategory;
    private String fuelType;
    private String vehicleStatus;
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "staffId")
    private StaffEntity staff;

}
