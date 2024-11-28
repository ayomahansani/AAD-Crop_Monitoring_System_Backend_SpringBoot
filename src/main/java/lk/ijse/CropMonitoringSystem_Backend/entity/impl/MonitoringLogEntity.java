package lk.ijse.CropMonitoringSystem_Backend.entity.impl;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lk.ijse.CropMonitoringSystem_Backend.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "monitoring-log")
public class MonitoringLogEntity implements SuperEntity {

    @Id
    private String logCode;
    private LocalDate logDate;
    private String logDetails;
    @Column(columnDefinition = "LONGTEXT")
    private String observedImage;

    @ManyToMany
    @JoinTable(
            name = "field-monitor-details",
            joinColumns = @JoinColumn(name = "logCode"),
            inverseJoinColumns = @JoinColumn(name = "fieldCode")
    )
    @JsonManagedReference // Manage the serialization
    private List<FieldEntity> fields;

    @ManyToMany
    @JoinTable(
            name = "crop-monitor-details",
            joinColumns = @JoinColumn(name = "logCode"),
            inverseJoinColumns = @JoinColumn(name = "cropCode")
    )
    @JsonManagedReference // Manage the serialization
    private List<CropEntity> crops;

    @ManyToMany
    @JoinTable(
            name = "staff-monitor-details",
            joinColumns = @JoinColumn(name = "logCode"),
            inverseJoinColumns = @JoinColumn(name = "staffId")
    )
    @JsonManagedReference // Manage the serialization
    private List<StaffEntity> staffMembers;

}
