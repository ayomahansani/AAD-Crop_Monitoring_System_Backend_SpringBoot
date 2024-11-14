package lk.ijse.CropMonitoringSystem_Backend.entity.impl;

import jakarta.persistence.*;
import lk.ijse.CropMonitoringSystem_Backend.entity.Gender;
import lk.ijse.CropMonitoringSystem_Backend.entity.Role;
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
@ToString
@Entity
@Table(name = "staff")
public class StaffEntity implements SuperEntity {

    @Id
    private String staffId;
    private String firstName;
    private String lastName;
    private String designation;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate joinedDate;
    private LocalDate dob;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String addressLine5;
    private String contactNo;
    @Column(unique = true)
    private String staffEmail;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserEntity user;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleEntity> vehicles;

    @ManyToMany(mappedBy = "staffMembers", cascade = CascadeType.ALL)
    private List<FieldEntity> fields;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<EquipmentEntity> equipments;

    @ManyToMany(mappedBy = "staffMembers", cascade = CascadeType.ALL)
    private List<MonitoringLogEntity> logs;

}
