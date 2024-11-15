package lk.ijse.CropMonitoringSystem_Backend.dto.impl;

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

    private String cropCode;
    private String cropCommonName;
    private String cropScientificName;
    private String cropImage;
    private String cropCategory;
    private String cropSeason;
    private String fieldCode;
    //private List<MonitoringLogDTO> logDTOS;
}
