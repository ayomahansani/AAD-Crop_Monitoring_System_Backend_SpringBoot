package lk.ijse.CropMonitoringSystem_Backend.dto.impl;

import jakarta.persistence.Id;
import lk.ijse.CropMonitoringSystem_Backend.dto.MonitoringLogStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class MonitoringLogDTO implements MonitoringLogStatus {

    @Id
    private String logCode;
    private LocalDate logDate;
    private String logDetails;
    private String observedImage;
    private List<FieldDTO> fieldDTOS;
    private List<CropDTO> cropDTOS;
    private List<StaffDTO> staffDTOS;
}
