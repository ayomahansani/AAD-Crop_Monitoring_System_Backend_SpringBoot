package lk.ijse.CropMonitoringSystem_Backend.entity.impl;

import jakarta.persistence.*;
import lk.ijse.CropMonitoringSystem_Backend.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name = "crop")
public class CropEntity implements SuperEntity {

    @Id
    private String cropCode;
    private String cropCommonName;
    private String cropScientificName;
    @Column(columnDefinition = "LONGTEXT")
    private String cropImage;
    private String cropCategory;
    private String cropSeason;

    @ManyToOne
    @JoinColumn(name = "fieldCode")
    private FieldEntity field;

    @ManyToMany(mappedBy = "crops", cascade = CascadeType.ALL)
    private List<MonitoringLogEntity> logs;

}
