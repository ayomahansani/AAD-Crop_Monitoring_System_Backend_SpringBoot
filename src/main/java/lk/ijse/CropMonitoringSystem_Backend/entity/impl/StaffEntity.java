package lk.ijse.CropMonitoringSystem_Backend.entity.impl;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private String address;
    private String contactNo;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserEntity user;

    @ManyToMany
    @JoinTable(
            name = "staff-fields-details",
            joinColumns = @JoinColumn(name = "staffId"),
            inverseJoinColumns = @JoinColumn(name = "fieldCode")
    )
    @JsonManagedReference // Manage the serialization
    private List<FieldEntity> fields;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EquipmentEntity> equipments;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleEntity> vehicles;

    @ManyToMany(mappedBy = "staffMembers")
    @JsonBackReference
    private List<MonitoringLogEntity> logs;

}
