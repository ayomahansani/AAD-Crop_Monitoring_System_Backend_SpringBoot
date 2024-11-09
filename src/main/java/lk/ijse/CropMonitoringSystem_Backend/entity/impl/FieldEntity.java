package lk.ijse.CropMonitoringSystem_Backend.entity.impl;

import jakarta.persistence.*;
import lk.ijse.CropMonitoringSystem_Backend.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.awt.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name = "Field")
public class FieldEntity implements SuperEntity {

    @Id
    private String fieldCode;
    private String fieldName;
    private Point fieldLocation;
    private double extentsize;
    @Column(columnDefinition = "LONGTEXT")
    private String fieldImage1;
    @Column(columnDefinition = "LONGTEXT")
    private String fieldImage2;

    @OneToMany(mappedBy = "field")
    private List<CropEntity> crops;

    @OneToMany(mappedBy = "field")
    private List<EquipmentEntity> equipments;

    @ManyToMany(mappedBy = "fields")
    private List<MonitoringLogEntity> logs;

    @ManyToMany(mappedBy = "fields")
    private List<StaffEntity> staffMembers;

}
