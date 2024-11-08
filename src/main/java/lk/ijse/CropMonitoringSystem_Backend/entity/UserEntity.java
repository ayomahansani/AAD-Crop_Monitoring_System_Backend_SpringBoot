package lk.ijse.CropMonitoringSystem_Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name = "User")

public class UserEntity implements SuperEntity{

    @Id
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

}
