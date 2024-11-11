package lk.ijse.CropMonitoringSystem_Backend.util;

import lk.ijse.CropMonitoringSystem_Backend.dto.impl.*;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapping {

    @Autowired
    private ModelMapper modelMapper;


    // For User Mapping
    public UserEntity toUserEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserEntity.class);
    }
    public UserDTO toUserDTO(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDTO.class);
    }
    public List<UserDTO> toUserDTOList(List<UserEntity> userEntityList) {
        return modelMapper.map(userEntityList, new TypeToken<List<UserDTO>>() {}.getType());
    }


    // For Crop Mapping
    public CropEntity toCropEntity(CropDTO cropDTO) {
        return modelMapper.map(cropDTO, CropEntity.class);
    }
    public CropDTO toCropDTO(CropEntity cropEntity) {
        return modelMapper.map(cropEntity, CropDTO.class);
    }
    public List<CropDTO> toCropDTOList(List<CropEntity> cropEntityList) {
        return modelMapper.map(cropEntityList, new TypeToken<List<CropDTO>>() {}.getType());
    }


    // For Field Mapping
    public FieldEntity toFieldEntity(FieldDTO fieldDTO) {
        return modelMapper.map(fieldDTO, FieldEntity.class);
    }
    public FieldDTO toFieldDTO(FieldEntity fieldEntity) {
        return modelMapper.map(fieldEntity, FieldDTO.class);
    }
    public List<FieldDTO> toFieldDTOList(List<FieldEntity> fieldEntityList) {
        return modelMapper.map(fieldEntityList, new TypeToken<List<FieldDTO>>() {}.getType());
    }


    // For Staff Mapping
    public StaffEntity toStaffEntity(StaffDTO staffDTO) {
        return modelMapper.map(staffDTO, StaffEntity.class);
    }
    public StaffDTO toStaffDTO(StaffEntity staffEntity) {
        return modelMapper.map(staffEntity, StaffDTO.class);
    }
    public List<StaffDTO> toStaffDTOList(List<StaffEntity> staffEntityList) {
        return modelMapper.map(staffEntityList, new TypeToken<List<StaffDTO>>() {}.getType());
    }


    // For Vehicle Mapping
    public VehicleEntity toVehicleEntity(VehicleDTO vehicleDTO) {
        return modelMapper.map(vehicleDTO, VehicleEntity.class);
    }
    public VehicleDTO toVehicleDTO(VehicleEntity vehicleEntity) {
        return modelMapper.map(vehicleEntity, VehicleDTO.class);
    }
    public List<VehicleDTO> toVehicleDTOList(List<VehicleEntity> vehicleEntityList) {
        return modelMapper.map(vehicleEntityList, new TypeToken<List<VehicleDTO>>() {}.getType());
    }


    // For Equipment Mapping
    public EquipmentEntity toEquipmentEntity(EquipmentDTO equipmentDTO) {
        return modelMapper.map(equipmentDTO, EquipmentEntity.class);
    }
    public EquipmentDTO toEquipmentDTO(EquipmentEntity equipmentEntity) {
        return modelMapper.map(equipmentEntity, EquipmentDTO.class);
    }
    public List<EquipmentDTO> toEquipmentDTOList(List<EquipmentEntity> equipmentEntityList) {
        return modelMapper.map(equipmentEntityList, new TypeToken<List<EquipmentDTO>>() {}.getType());
    }


    // For MonitoringLog Mapping
    public MonitoringLogEntity toMonitoringLogEntity(MonitoringLogDTO monitoringLogDTO) {
        return modelMapper.map(monitoringLogDTO, MonitoringLogEntity.class);
    }
    public MonitoringLogDTO toMonitoringLogDTO(MonitoringLogEntity monitoringLogEntity) {
        return modelMapper.map(monitoringLogEntity, MonitoringLogDTO.class);
    }
    public List<MonitoringLogDTO> toMonitoringLogDTOList(List<MonitoringLogEntity> monitoringLogEntityList) {
        return modelMapper.map(monitoringLogEntityList, new TypeToken<List<MonitoringLogDTO>>() {}.getType());
    }

}
