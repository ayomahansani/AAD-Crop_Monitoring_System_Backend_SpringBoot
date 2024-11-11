package lk.ijse.CropMonitoringSystem_Backend.dto.impl;

import jakarta.persistence.Id;
import lk.ijse.CropMonitoringSystem_Backend.dto.CropStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CropDTO implements CropStatus {

    @Id
    private String cropCode;
    private String cropCommonName;
    private String cropScientificName;
    private String cropImage;
    private String cropCategory;
    private String cropSeason;
    private FieldDTO fieldDTO;
    private List<MonitoringLogDTO> logDTOS;
}
