package lk.ijse.CropMonitoringSystem_Backend.dto.impl;

import jakarta.persistence.*;
import lk.ijse.CropMonitoringSystem_Backend.dto.UserStatus;
import lk.ijse.CropMonitoringSystem_Backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserDTO implements UserStatus {

    @Id
    private String email;
    private String password;
    private Role role;
    private StaffDTO staffDTO;
}
