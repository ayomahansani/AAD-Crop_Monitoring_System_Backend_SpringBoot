package lk.ijse.CropMonitoringSystem_Backend.entity.impl;

import jakarta.persistence.*;
import lk.ijse.CropMonitoringSystem_Backend.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.geo.Point;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name = "field")
public class FieldEntity implements SuperEntity {

    @Id
    private String fieldCode;
    private String fieldName;
    private Point fieldLocation;
    private double fieldExtentsize;
    @Column(columnDefinition = "LONGTEXT")
    private String fieldImage1;
    @Column(columnDefinition = "LONGTEXT")
    private String fieldImage2;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CropEntity> crops;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    private List<EquipmentEntity> equipments;

    @ManyToMany(mappedBy = "fields", cascade = CascadeType.ALL)
    private List<MonitoringLogEntity> logs;

    @ManyToMany
    @JoinTable(
            name = "field-staff-details",
            joinColumns = @JoinColumn(name = "fieldCode"),
            inverseJoinColumns = @JoinColumn(name = "staffId")
    )
    private List<StaffEntity> staffMembers;

}
