package lk.ijse.CropMonitoringSystem_Backend.dto.impl;

import jakarta.persistence.Id;
import lk.ijse.CropMonitoringSystem_Backend.dto.StaffStatus;
import lk.ijse.CropMonitoringSystem_Backend.entity.Gender;
import lk.ijse.CropMonitoringSystem_Backend.entity.Role;
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
public class StaffDTO implements StaffStatus {

    @Id
    private String staffId;
    private String firstName;
    private String lastName;
    private String designation;
    private Gender gender;
    private LocalDate joinedDate;
    private LocalDate dob;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String addressLine5;
    private String contactNo;
    private String staffEmail;
    private Role role;
    private UserDTO userDTO;
    private List<VehicleDTO> vehicleDTOS;
    private List<FieldDTO> fieldDTOS;
    private List<EquipmentDTO> equipmentDTOS;
    private List<MonitoringLogDTO> logDTOS;
}
