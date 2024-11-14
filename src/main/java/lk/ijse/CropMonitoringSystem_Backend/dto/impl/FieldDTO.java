package lk.ijse.CropMonitoringSystem_Backend.dto.impl;

import jakarta.persistence.Id;
import lk.ijse.CropMonitoringSystem_Backend.dto.FieldStatus;
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
public class FieldDTO implements FieldStatus {

    @Id
    private String fieldCode;
    private String fieldName;
    private Point fieldLocation;
    private double fieldExtentsize;
    private String fieldImage1;
    private String fieldImage2;
    private List<String> staffIds; // Many-To-Many
    /*private List<CropDTO> cropDTOS;
    private List<StaffDTO> staffDTOS;
    private List<EquipmentDTO> equipmentDTOS;
    private List<MonitoringLogDTO> logDTOS;*/
}
