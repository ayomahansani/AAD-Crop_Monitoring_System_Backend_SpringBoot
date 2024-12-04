package lk.ijse.CropMonitoringSystem_Backend.service.impl;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.EquipmentNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.FieldNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.StaffNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dao.EquipmentDAO;
import lk.ijse.CropMonitoringSystem_Backend.dao.FieldDAO;
import lk.ijse.CropMonitoringSystem_Backend.dao.StaffDAO;
import lk.ijse.CropMonitoringSystem_Backend.dto.EquipmentStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.EquipmentDTO;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.EquipmentEntity;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.FieldEntity;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.StaffEntity;
import lk.ijse.CropMonitoringSystem_Backend.service.EquipmentService;
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
public class EquipmentServiceIMPL implements EquipmentService {

    @Autowired
    private EquipmentDAO equipmentDAO;

    @Autowired
    private StaffDAO staffDAO;

    @Autowired
    private FieldDAO fieldDAO;

    @Autowired
    private Mapping mapping;


    // save equipment
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR')")
    public void saveEquipment(EquipmentDTO equipmentDTO) {
        equipmentDTO.setEquipmentId(AppUtil.generateCode("EQUIPMENT"));

        String fieldCode = equipmentDTO.getFieldCode();
        FieldEntity fieldEntity = fieldDAO.findById(fieldCode) // get field entity using field code
                .orElseThrow(() -> new FieldNotFoundException("Field not found with ID: " + fieldCode));

        String staffId = equipmentDTO.getStaffId();
        StaffEntity staffEntity = staffDAO.findById(staffId) // get staff entity using staff id
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with ID: " + staffId));

        EquipmentEntity equipmentEntity = mapping.toEquipmentEntity(equipmentDTO);
        equipmentEntity.setField(fieldEntity);
        equipmentEntity.setStaff(staffEntity);

        EquipmentEntity savedEquipment = equipmentDAO.save(equipmentEntity);

        if(savedEquipment == null){
            throw new DataPersistException("Equipment not saved");
        }
    }

    // get selected equipment
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public EquipmentStatus getSelectedEquipment(String equipmentId) {
        if (equipmentDAO.existsById(equipmentId)) {
            EquipmentEntity selectedEquipment = equipmentDAO.getReferenceById(equipmentId);
            return mapping.toEquipmentDTO(selectedEquipment);
        } else {
            return new SelectedErrorStatus(2, "Selected equipment not found");
        }
    }

    // get all equipments
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public List<EquipmentDTO> getAllEquipments() {
        List<EquipmentEntity> equipmentEntityList = equipmentDAO.findAll();
        return mapping.toEquipmentDTOList(equipmentEntityList);
    }

    // delete equipment
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR')")
    public void deleteEquipment(String equipmentId) {
        Optional<EquipmentEntity> foundEquipment = equipmentDAO.findById(equipmentId);
        if (!foundEquipment.isPresent()) {
            throw new EquipmentNotFoundException("Equipment not found");
        } else {
            equipmentDAO.deleteById(equipmentId);
        }
    }

    // update equipment
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR')")
    public void updateEquipment(String equipmentId, EquipmentDTO updatedEquipmentDTO) {
        Optional<EquipmentEntity> foundEquipment = equipmentDAO.findById(equipmentId);
        if (!foundEquipment.isPresent()) {
            throw new EquipmentNotFoundException("Equipment not found");
        } else {
            foundEquipment.get().setEquipmentName(updatedEquipmentDTO.getEquipmentName());
            foundEquipment.get().setEquipmentStatus(updatedEquipmentDTO.getEquipmentStatus());
            foundEquipment.get().setEquipmentType(updatedEquipmentDTO.getEquipmentType());

            String fieldCode = updatedEquipmentDTO.getFieldCode();
            FieldEntity fieldEntity = fieldDAO.findById(fieldCode) // get field entity using field code
                    .orElseThrow(() -> new FieldNotFoundException("Field not found with ID: " + fieldCode));

            String staffId = updatedEquipmentDTO.getStaffId();
            StaffEntity staffEntity = staffDAO.findById(staffId) // get staff entity using staff id
                    .orElseThrow(() -> new StaffNotFoundException("Staff not found with ID: " + staffId));

            foundEquipment.get().setField(fieldEntity);
            foundEquipment.get().setStaff(staffEntity);
        }
    }

    // get equipment related to a staff
    @Override
    public List<EquipmentDTO> getEquipmentByStaffId(String staffId) {
        StaffEntity staffEntity = staffDAO.findById(staffId)
                .orElseThrow(() -> new StaffNotFoundException("Staff not found with ID: " + staffId));
        List<EquipmentEntity> equipmentEntityList = equipmentDAO.findByStaff(staffEntity);
        return mapping.toEquipmentDTOList(equipmentEntityList);
    }

    // get equipment related to a field
    @Override
    public List<EquipmentDTO> getEquipmentByFieldId(String fieldCode) {
        FieldEntity fieldEntity = fieldDAO.findById(fieldCode)
                .orElseThrow(() -> new FieldNotFoundException("Field not found with code: " + fieldCode));
        List<EquipmentEntity> equipmentList = equipmentDAO.findByField(fieldEntity);
        return mapping.toEquipmentDTOList(equipmentList);
    }

}
