package lk.ijse.CropMonitoringSystem_Backend.entity.impl;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToMany(mappedBy = "fields", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<StaffEntity> staffMembers;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CropEntity> crops;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EquipmentEntity> equipments;

    @ManyToMany(mappedBy = "fields", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<MonitoringLogEntity> logs;


}
