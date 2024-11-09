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
@Table(name = "Equipment")
public class EquipmentEntity implements SuperEntity {

    @Id
    private String equipmentId;
    private String equipmentName;
    private String equipmentType;
    private String EquipmentStatus;

    @ManyToOne
    @JoinColumn(name = "fieldCode", nullable = false)
    private FieldEntity field;

    @ManyToOne
    @JoinColumn(name = "staffId", nullable = false)
    private StaffEntity staff;

}
