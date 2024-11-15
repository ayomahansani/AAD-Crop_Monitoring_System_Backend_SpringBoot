package lk.ijse.CropMonitoringSystem_Backend.dto.impl;

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

    private String logCode;
    private LocalDate logDate;
    private String logDetails;
    private String observedImage;

    private List<String> fieldCodes; // Many-To-Many -> Log & Field
    private List<String> cropCodes; // Many-To-Many -> Log & Crop
    private List<String> staffIds; // Many-To-Many -> Log & Staff

}
