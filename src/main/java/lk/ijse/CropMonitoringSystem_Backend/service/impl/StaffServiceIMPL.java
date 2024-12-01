package lk.ijse.CropMonitoringSystem_Backend.service.impl;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.FieldNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.StaffNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dao.FieldDAO;
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

import java.util.*;

@Service
@Transactional
public class StaffServiceIMPL implements StaffService {

    @Autowired
    private StaffDAO staffDAO;

    @Autowired
    private FieldDAO fieldDAO;

    @Autowired
    private Mapping mapping;


    // save staff
    @Override
    public StaffDTO saveStaff(StaffDTO staffDTO) {

        staffDTO.setStaffId(AppUtil.generateCode("STAFF"));
        try {
            StaffEntity staffEntity = mapping.toStaffEntity(staffDTO);

            if (staffDTO.getFieldIds() != null && !staffDTO.getFieldIds().isEmpty()) {
                // Retrieve and associate fields
                List<FieldEntity> associatedFields = new ArrayList<>();

                for (String fieldId : staffDTO.getFieldIds()) {
                    FieldEntity field = fieldDAO.findById(fieldId)
                            .orElseThrow(() -> new FieldNotFoundException("Field not found with ID: " + fieldId));
                    // Add the field to the fieldEntities list
                    associatedFields.add(field);
                }
                staffEntity.setFields(associatedFields);
            }
            // Save the staff entity
            StaffEntity savedStaff = staffDAO.save(staffEntity);
            return mapping.toStaffDTO(savedStaff);
        } catch (Exception e) {
            throw new RuntimeException("Error saving staff: " + e.getMessage(), e);
        }
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

        StaffEntity staff = staffDAO.findById(staffId)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with ID: " + staffId));

        // Remove associations with staff members
        staff.getLogs().forEach(log -> log.getStaffMembers().remove(staff));
        staff.getLogs().clear();

        staffDAO.deleteById(staffId);
    }

    // update staff
    @Transactional
    @Override
    public void updateStaff(String staffId, StaffDTO updatedStaffDTO) {
        StaffEntity foundStaff = staffDAO.findById(staffId)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with ID: " + staffId));

        // Update basic fields
        foundStaff.setFirstName(updatedStaffDTO.getFirstName());
        foundStaff.setLastName(updatedStaffDTO.getLastName());
        foundStaff.setDesignation(updatedStaffDTO.getDesignation());
        foundStaff.setGender(updatedStaffDTO.getGender());
        foundStaff.setJoinedDate(updatedStaffDTO.getJoinedDate());
        foundStaff.setDob(updatedStaffDTO.getDob());
        foundStaff.setAddress(updatedStaffDTO.getAddress());
        foundStaff.setContactNo(updatedStaffDTO.getContactNo());
        foundStaff.setRole(updatedStaffDTO.getRole());
        foundStaff.setEmail(updatedStaffDTO.getEmail());

        // Update fields
        if (updatedStaffDTO.getFieldIds() != null && !updatedStaffDTO.getFieldIds().isEmpty()) {
            // Clear current fields association
            foundStaff.getFields().clear();

            List<String> fieldIds = updatedStaffDTO.getFieldIds();

            // Retrieve and associate fields
            for (String fieldId : fieldIds) {
                FieldEntity field = fieldDAO.findById(fieldId)
                        .orElseThrow(() -> new FieldNotFoundException("Field not found with ID: " + fieldId));

                // Add staff to field's staff list (bidirectional side)
                if (!field.getStaffMembers().contains(foundStaff)) {
                    field.getStaffMembers().add(foundStaff);
                }

                // Add the field to the staff's fields list
                foundStaff.getFields().add(field);
            }
        } else {
            // If no fields are provided, clear the association
            foundStaff.getFields().clear();
        }

        // Save the updated staff entity
        staffDAO.save(foundStaff);
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