package lk.ijse.CropMonitoringSystem_Backend.entity.impl;

import jakarta.persistence.*;
import lk.ijse.CropMonitoringSystem_Backend.entity.Gender;
import lk.ijse.CropMonitoringSystem_Backend.entity.Role;
import lk.ijse.CropMonitoringSystem_Backend.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Entity
@Table(name = "Staff")
public class StaffEntity implements SuperEntity {

    @Id
    private String staffId;
    private String firstName;
    private String lastName;
    private String designation;
    private Gender gender;
    private Date joinedDate;
    private Date dob;
    private String address;
    private String contactNo;
    private String staffEmail;
    private Role role;

    @OneToMany(mappedBy = "staff")
    private List<EquipmentEntity> equipments;

    @ManyToMany(mappedBy = "staffMembers")
    private List<MonitoringLogEntity> logs;

    @ManyToMany
    private List<FieldEntity> fields;

}