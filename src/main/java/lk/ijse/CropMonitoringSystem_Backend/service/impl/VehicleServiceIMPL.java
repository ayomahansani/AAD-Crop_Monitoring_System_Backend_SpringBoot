package lk.ijse.CropMonitoringSystem_Backend.service.impl;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.StaffNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.VehicleNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dao.StaffDAO;
import lk.ijse.CropMonitoringSystem_Backend.dao.VehicleDAO;
import lk.ijse.CropMonitoringSystem_Backend.dto.VehicleStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.VehicleDTO;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.StaffEntity;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.VehicleEntity;
import lk.ijse.CropMonitoringSystem_Backend.service.VehicleService;
import lk.ijse.CropMonitoringSystem_Backend.util.AppUtil;
import lk.ijse.CropMonitoringSystem_Backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehicleServiceIMPL implements VehicleService {

    @Autowired
    private VehicleDAO vehicleDAO;

    @Autowired
    private StaffDAO staffDAO;

    @Autowired
    private Mapping mapping;


    // save vehicle
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR')")
    public void saveVehicle(VehicleDTO vehicleDTO) {
        vehicleDTO.setVehicleCode(AppUtil.generateCode("VEHICLE"));

        String staffId = vehicleDTO.getStaffId();
        StaffEntity staffEntity = staffDAO.findById(staffId) // get staff entity using staff id
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with ID: " + staffId));

        VehicleEntity vehicleEntity = mapping.toVehicleEntity(vehicleDTO);
        vehicleEntity.setStaff(staffEntity);

        VehicleEntity savedVehicle = vehicleDAO.save(vehicleEntity);

        if(savedVehicle == null){
            throw new DataPersistException("Vehicle not saved");
        }
    }

    // get selected vehicle
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public VehicleStatus getSelectedVehicle(String vehicleCode) {
        if(vehicleDAO.existsById(vehicleCode)) {
            VehicleEntity selectedVehicle = vehicleDAO.getReferenceById(vehicleCode);
            return mapping.toVehicleDTO(selectedVehicle);
        } else {
            return new SelectedErrorStatus(2, "Selected vehicle not found");
        }
    }

    // get all vehicles
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public List<VehicleDTO> getAllVehicles() {
        List<VehicleEntity> vehicleEntityList = vehicleDAO.findAll();
        return mapping.toVehicleDTOList(vehicleEntityList);
    }

    // delete vehicle
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR')")
    public void deleteVehicle(String vehicleCode) {
        Optional<VehicleEntity> foundVehicle = vehicleDAO.findById(vehicleCode);
        if(!foundVehicle.isPresent()){
            throw new VehicleNotFoundException("Vehicle not found");
        } else {
            vehicleDAO.deleteById(vehicleCode);
        }
    }

    // update vehicle
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR')")
    public void updateVehicle(String vehicleCode, VehicleDTO updatedVehicleDTO) {
        Optional<VehicleEntity> foundVehicle = vehicleDAO.findById(vehicleCode);
        if(!foundVehicle.isPresent()){
            throw new VehicleNotFoundException("Vehicle not found");
        } else {
            foundVehicle.get().setVehicleCategory(updatedVehicleDTO.getVehicleCategory());
            foundVehicle.get().setVehicleStatus(updatedVehicleDTO.getVehicleStatus());
            foundVehicle.get().setFuelType(updatedVehicleDTO.getFuelType());
            foundVehicle.get().setLicensePlateNumber(updatedVehicleDTO.getLicensePlateNumber());
            foundVehicle.get().setRemarks(updatedVehicleDTO.getRemarks());

            String staffId = updatedVehicleDTO.getStaffId();
            StaffEntity staffEntity = staffDAO.findById(staffId) // get staff entity using staff id
                    .orElseThrow(() -> new StaffNotFoundException("Staff not found with ID: " + staffId));

            foundVehicle.get().setStaff(staffEntity);
        }
    }

    // get vehicles related to a staff member
    @Override
    public List<VehicleDTO> getVehiclesByStaffId(String staffId) {
        StaffEntity staffEntity = staffDAO.findById(staffId)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with ID: " + staffId));
        List<VehicleEntity> vehicleEntityList = vehicleDAO.findByStaff(staffEntity);
        return mapping.toVehicleDTOList(vehicleEntityList);
    }

}
