package lk.ijse.CropMonitoringSystem_Backend.service.impl;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.FieldNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.StaffNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dao.FieldDAO;
import lk.ijse.CropMonitoringSystem_Backend.dao.StaffDAO;
import lk.ijse.CropMonitoringSystem_Backend.dto.FieldStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.FieldDTO;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.StaffDTO;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.FieldEntity;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.StaffEntity;
import lk.ijse.CropMonitoringSystem_Backend.service.FieldService;
import lk.ijse.CropMonitoringSystem_Backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FieldServiceIMPL implements FieldService {

    @Autowired
    private FieldDAO fieldDAO;

    @Autowired
    private StaffDAO staffDAO;

    @Autowired
    private Mapping mapping;


    // save field
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public void saveField(FieldDTO fieldDTO) {
        FieldEntity fieldEntity = mapping.toFieldEntity(fieldDTO);
        List<StaffEntity> staffEntityList = new ArrayList<>(); // create new StaffEntity List
        List<String> staffIds = fieldDTO.getStaffIds(); // our dto has staffId List . But we want StaffEntity List
        if(staffIds != null){
            for (String staffId : staffIds) {
                StaffEntity staffEntity = staffDAO.findById(staffId) // so get staffEntity one by one using staffIds
                        .orElseThrow(() -> new StaffNotFoundException("Staff not found with ID: " + staffId));

                staffEntityList.add(staffEntity); // add them to newly created StaffEntity List
            }
        }
        fieldEntity.setStaffMembers(staffEntityList); // set that StaffEntity List to the entity
        FieldEntity savedField = fieldDAO.save(fieldEntity);

        if(savedField == null){
            throw new DataPersistException("Field not saved");
        }
    }

    // get selected field
    @Override
    public FieldStatus getSelectedField(String fieldCode) {
        if (fieldDAO.existsById(fieldCode)) {
            FieldEntity selectedField = fieldDAO.getReferenceById(fieldCode);
            return mapping.toFieldDTO(selectedField);
        } else {
            return new SelectedErrorStatus(2, "Selected field not found");
        }
    }

    // get all fields
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public List<FieldDTO> getAllFields() {
        List<FieldEntity> fieldEntityList = fieldDAO.findAll();
        return mapping.toFieldDTOList(fieldEntityList);
    }

    // delete field
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public void deleteField(String fieldCode) {
        Optional<FieldEntity> foundField = fieldDAO.findById(fieldCode);
        if(!foundField.isPresent()){
            throw new FieldNotFoundException("Field not found");
        } else {
            fieldDAO.deleteById(fieldCode);
        }
    }

    // update field
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public void updateField(String fieldCode, FieldDTO updatedFieldDTO) {
        Optional<FieldEntity> foundField = fieldDAO.findById(fieldCode);
        if(!foundField.isPresent()){
            throw new FieldNotFoundException("Field not found with ID: " + fieldCode);
        } else {
            // Update basic field properties
            foundField.get().setFieldName(updatedFieldDTO.getFieldName());
            foundField.get().setFieldLocation(updatedFieldDTO.getFieldLocation());
            foundField.get().setFieldExtentsize(updatedFieldDTO.getFieldExtentsize());

            // Update associated staff members
            if(updatedFieldDTO.getStaffIds() != null && !updatedFieldDTO.getStaffIds().isEmpty()){
                List<String> staffIds = updatedFieldDTO.getStaffIds();
                List<StaffEntity> staffEntityList = staffDAO.findAllById(staffIds);
                if(staffEntityList.size() != updatedFieldDTO.getStaffIds().size()){
                    throw new IllegalArgumentException("One or more staff IDs are invalid.");
                }
                foundField.get().setStaffMembers(staffEntityList);
            } else {
                foundField.get().getStaffMembers().clear(); // Clear existing staff if no IDs are provided
            }

            // Handle images if provided
            if(updatedFieldDTO.getFieldImage1() != null){
                foundField.get().setFieldImage1(updatedFieldDTO.getFieldImage1());
            }

            if(updatedFieldDTO.getFieldImage2() != null){
                foundField.get().setFieldImage2(updatedFieldDTO.getFieldImage2());
            }
        }
    }

    // get staff members related to a field
    @Override
    //@PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public List<StaffDTO> getStaffIdsByFieldCode(String fieldCode) {
        FieldEntity fieldEntity = fieldDAO.findById(fieldCode)
                .orElseThrow(() -> new FieldNotFoundException("Field not found with ID: " + fieldCode));

        List<StaffEntity> staffEntityList = fieldEntity.getStaffMembers();
        return mapping.toStaffDTOList(staffEntityList);
    }

}
