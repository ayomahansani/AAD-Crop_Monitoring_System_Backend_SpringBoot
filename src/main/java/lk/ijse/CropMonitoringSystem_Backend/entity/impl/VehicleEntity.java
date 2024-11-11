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
@ToString
@Entity
@Table(name = "Vehicle")
public class VehicleEntity implements SuperEntity {

    @Id
    private String vehicleCode;
    private String licensePlateNumber;
    private String vehicleCategory;
    private String fuelType;
    private String vehicleStatus;
    private String remarks;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "staffId", nullable = false)
    private StaffEntity staff;

}
