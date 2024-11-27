package lk.ijse.CropMonitoringSystem_Backend.service.impl;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.StaffNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dao.StaffDAO;
import lk.ijse.CropMonitoringSystem_Backend.dto.StaffStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.FieldDTO;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.StaffDTO;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.FieldEntity;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.StaffEntity;
import lk.ijse.CropMonitoringSystem_Backend.service.StaffService;
import lk.ijse.CropMonitoringSystem_Backend.util.AppUtil;
import lk.ijse.CropMonitoringSystem_Backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StaffServiceIMPL implements StaffService {

    @Autowired
    private StaffDAO staffDAO;

    @Autowired
    private Mapping mapping;


    // save staff
    @Override
    public StaffDTO saveStaff(StaffDTO staffDTO) {
        staffDTO.setStaffId(AppUtil.generateCode("STAFF"));
        StaffEntity savedStaff = staffDAO.save(mapping.toStaffEntity(staffDTO));
        if (savedStaff == null) {
            throw new DataPersistException("Staff not saved");
        }
        return mapping.toStaffDTO(savedStaff);
    }

    // get selected staff
    @Override
    public StaffStatus getSelectedStaff(String staffId) {
        if (staffDAO.existsById(staffId)) {
            StaffEntity selectedStaff = staffDAO.getReferenceById(staffId);
            return mapping.toStaffDTO(selectedStaff);
        } else {
            return new SelectedErrorStatus(2, "Selected staff not found");
        }
    }

    // get all staffs
    @Override
    public List<StaffDTO> getAllStaffs() {
        List<StaffEntity> staffEntityList = staffDAO.findAll();
        return mapping.toStaffDTOList(staffEntityList);
    }

    // delete staff
    @Override
    public void deleteStaff(String staffId) {
        Optional<StaffEntity> foundStaff = staffDAO.findById(staffId);
        if (!foundStaff.isPresent()) {
            throw new StaffNotFoundException("Staff not found");
        } else {
            staffDAO.deleteById(staffId);
        }
    }

    // update staff
    @Override
    public void updateStaff(String staffId, StaffDTO updatedStaffDTO) {
        Optional<StaffEntity> foundStaff = staffDAO.findById(staffId);
        if (!foundStaff.isPresent()) {
            throw new StaffNotFoundException("Staff not found");
        } else {
            foundStaff.get().setFirstName(updatedStaffDTO.getFirstName());
            foundStaff.get().setLastName(updatedStaffDTO.getLastName());
            foundStaff.get().setDesignation(updatedStaffDTO.getDesignation());
            foundStaff.get().setGender(updatedStaffDTO.getGender());
            foundStaff.get().setJoinedDate(updatedStaffDTO.getJoinedDate());
            foundStaff.get().setDob(updatedStaffDTO.getDob());
            foundStaff.get().setAddress(updatedStaffDTO.getAddress());
            foundStaff.get().setContactNo(updatedStaffDTO.getContactNo());
            foundStaff.get().setRole(updatedStaffDTO.getRole());
            foundStaff.get().setEmail(updatedStaffDTO.getEmail());
        }
    }

    // find staff by email
    @Override
    public Optional<StaffDTO> findByEmail(String email) {
        Optional<StaffEntity> staffByEmail = staffDAO.findByEmail(email);
        return staffByEmail.map(mapping::toStaffDTO);
    }

    // get fields related to a staff member
    @Override
    public List<FieldDTO> getFieldsByStaffId(String staffId) {
        StaffEntity staffEntity = staffDAO.findById(staffId)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with ID: " + staffId));

        List<FieldEntity> fieldEntityList = staffEntity.getFields();
        return mapping.toFieldDTOList(fieldEntityList);
    }

}
