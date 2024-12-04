package lk.ijse.CropMonitoringSystem_Backend.service.impl;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.*;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dao.CropDAO;
import lk.ijse.CropMonitoringSystem_Backend.dao.FieldDAO;
import lk.ijse.CropMonitoringSystem_Backend.dao.MonitoringLogDAO;
import lk.ijse.CropMonitoringSystem_Backend.dao.StaffDAO;
import lk.ijse.CropMonitoringSystem_Backend.dto.MonitoringLogStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.CropDTO;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.FieldDTO;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.MonitoringLogDTO;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.StaffDTO;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.CropEntity;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.FieldEntity;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.MonitoringLogEntity;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.StaffEntity;
import lk.ijse.CropMonitoringSystem_Backend.service.MonitoringLogService;
import lk.ijse.CropMonitoringSystem_Backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class MonitoringLogServiceIMPL implements MonitoringLogService {

    @Autowired
    private MonitoringLogDAO monitoringLogDAO;

    @Autowired
    private FieldDAO fieldDAO;

    @Autowired
    private CropDAO cropDAO;

    @Autowired
    private StaffDAO staffDAO;

    @Autowired
    private Mapping mapping;


    // save log
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public void saveMonitoringLog(MonitoringLogDTO monitoringLogDTO) {
        MonitoringLogEntity logEntity = mapping.toMonitoringLogEntity(monitoringLogDTO);

        // -------- Log-Field --------
        List<FieldEntity> fieldEntityList = new ArrayList<>(); // create new FieldEntity List
        List<String> fieldCodes = monitoringLogDTO.getFieldCodes(); // our dto has fieldCode List . But we want FieldEntity List
        if(fieldCodes != null && !fieldCodes.isEmpty()){
            for (String fieldCode : fieldCodes) {
                FieldEntity fieldEntity = fieldDAO.findById(fieldCode) // so get FieldEntity one by one using fieldCodes
                        .orElseThrow(() -> new FieldNotFoundException("Field not found with ID: " + fieldCode));

                fieldEntityList.add(fieldEntity); // add them to newly created FieldEntity List
            }
        }
        logEntity.setFields(fieldEntityList); // set that FieldEntity List to the entity


        // -------- Log-Crop --------
        List<CropEntity> cropEntityList = new ArrayList<>(); // create new CropEntity List
        List<String> cropCodes = monitoringLogDTO.getCropCodes(); // our dto has cropCode List . But we want CropEntity List
        if(cropCodes != null && !cropCodes.isEmpty()){
            for (String cropCode : cropCodes) {
                CropEntity cropEntity = cropDAO.findById(cropCode) // so get CropEntity one by one using cropCodes
                        .orElseThrow(() -> new CropNotFoundException("Crop not found with ID: " + cropCode));

                cropEntityList.add(cropEntity); // add them to newly created CropEntity List
            }
        }
        logEntity.setCrops(cropEntityList); // set that CropEntity List to the entity


        // -------- Log-Staff --------
        List<StaffEntity> staffEntityList = new ArrayList<>(); // create new StaffEntity List
        List<String> staffIds = monitoringLogDTO.getStaffIds(); // our dto has staffId List . But we want StaffEntity List
        if(staffIds != null && !staffIds.isEmpty()){
            for (String staffId : staffIds) {
                StaffEntity staffEntity = staffDAO.findById(staffId) // so get staffEntity one by one using staffIds
                        .orElseThrow(() -> new StaffNotFoundException("Staff not found with ID: " + staffId));

                staffEntityList.add(staffEntity); // add them to newly created StaffEntity List
            }
        }
        logEntity.setStaffMembers(staffEntityList); // set that StaffEntity List to the entity

        // save full entity
        MonitoringLogEntity savedLog = monitoringLogDAO.save(logEntity);

        if(savedLog == null){
            throw new DataPersistException("Log not saved");
        }

    }

    // get selected log
    @Override
    public MonitoringLogStatus getSelectedMonitoringLog(String logCode) {
        if(monitoringLogDAO.existsById(logCode)) {
            MonitoringLogEntity selectedLog = monitoringLogDAO.getReferenceById(logCode);
            return mapping.toMonitoringLogDTO(selectedLog);
        } else {
            return new SelectedErrorStatus(2, "Log not found");
        }
    }

    // get all logs
    @Override
    public List<MonitoringLogDTO> getAllMonitoringLogs() {
        List<MonitoringLogEntity> monitoringLogEntityList = monitoringLogDAO.findAll();
        return mapping.toMonitoringLogDTOList(monitoringLogEntityList);
    }

    // delete log
    @Override
    public void deleteMonitoringLog(String logCode) {
        Optional<MonitoringLogEntity> foundLog = monitoringLogDAO.findById(logCode);
        if(!foundLog.isPresent()){
            throw new MonitoringLogNotFoundException("Log not found");
        } else {
            monitoringLogDAO.deleteById(logCode);
        }
    }

    // update log
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public void updateMonitoringLog(String logCode, MonitoringLogDTO monitoringLogDTO) {
        MonitoringLogEntity foundLog = monitoringLogDAO.findById(logCode)
                .orElseThrow(() -> new IllegalArgumentException("Log not found with Code: " + logCode));

            // Update basic field properties
            foundLog.setLogDate(monitoringLogDTO.getLogDate());
            foundLog.setLogDetails(monitoringLogDTO.getLogDetails());

            // Handle image if provided
            if(monitoringLogDTO.getObservedImage() != null) {
                foundLog.setObservedImage(monitoringLogDTO.getObservedImage());
            }


            // Update associated fields
            if(monitoringLogDTO.getFieldCodes() != null && !monitoringLogDTO.getFieldCodes().isEmpty()){

                List<String> fieldCodes = monitoringLogDTO.getFieldCodes();
                List<FieldEntity> fieldEntityList = fieldDAO.findAllById(fieldCodes);
                if(fieldEntityList.size() != fieldCodes.size()){
                    throw new IllegalArgumentException("One or more field codes are invalid.");
                }

                // Add only new associations
                for (FieldEntity field : fieldEntityList) {
                    if (!foundLog.getFields().contains(field)) {
                        foundLog.getFields().add(field);
                        field.getLogs().add(foundLog); // Maintain bi-directional association
                    }
                }

                // Remove unreferenced associations
                foundLog.getFields().removeIf(field -> !fieldEntityList.contains(field));

            } else {
                foundLog.getFields().clear();
            }


            // Update associated crops
            if(monitoringLogDTO.getCropCodes() != null && !monitoringLogDTO.getCropCodes().isEmpty()){

                List<String> cropCodes = monitoringLogDTO.getCropCodes();
                List<CropEntity> cropEntityList = cropDAO.findAllById(cropCodes);
                if(cropEntityList.size() != cropCodes.size()){
                    throw new IllegalArgumentException("One or more crop codes are invalid.");
                }

                // Add only new associations
                for (CropEntity crop : cropEntityList) {
                    if (!foundLog.getCrops().contains(crop)) {
                        foundLog.getCrops().add(crop);
                        crop.getLogs().add(foundLog); // Maintain bi-directional association
                    }
                }

                // Remove unreferenced associations
                foundLog.getCrops().removeIf(crop -> !cropEntityList.contains(crop));

            } else {
                foundLog.getCrops().clear();
            }


            // Update associated staff members
            if(monitoringLogDTO.getStaffIds() != null && !monitoringLogDTO.getStaffIds().isEmpty()){

                List<String> staffIds = monitoringLogDTO.getStaffIds();
                List<StaffEntity> staffEntityList = staffDAO.findAllById(staffIds);
                if(staffEntityList.size() != staffIds.size()){
                    throw new IllegalArgumentException("One or more staff IDs are invalid.");
                }

                // Add only new associations
                for (StaffEntity staff : staffEntityList) {
                    if (!foundLog.getStaffMembers().contains(staff)) {
                        foundLog.getStaffMembers().add(staff);
                        staff.getLogs().add(foundLog); // Maintain bi-directional association
                    }
                }

                // Remove unreferenced associations
                foundLog.getStaffMembers().removeIf(staff -> !staffEntityList.contains(staff));

            } else {
                foundLog.getStaffMembers().clear();
            }

            monitoringLogDAO.save(foundLog);
    }


    // get related fields, crops, staffs
    @Override
    public Map<String, Object> getRelatedEntitiesAsDtos(String logCode) {

        Map<String, Object> relatedEntities = new HashMap<>();
        List<FieldDTO> fieldDtos = null;
        List<CropDTO> cropDtos=null;
        List<StaffDTO> staffDtos =null;
        Optional<MonitoringLogEntity> logEntity = monitoringLogDAO.findById(logCode);

        if (logEntity.isPresent()){
            MonitoringLogEntity monitoringLogEntity = logEntity.get();
            // Convert PersistentSet to List
            List<FieldEntity> fieldEntities = new ArrayList<>(monitoringLogEntity.getFields());
            List<CropEntity> cropEntities = new ArrayList<>(monitoringLogEntity.getCrops());
            List<StaffEntity> staffEntities = new ArrayList<>(monitoringLogEntity.getStaffMembers());
            if (!fieldEntities.isEmpty()){
                fieldDtos =  mapping.toFieldDTOList(fieldEntities);
            }
            if (!cropEntities.isEmpty()){
                cropDtos = mapping.toCropDTOList(cropEntities);
            }
            if (!staffEntities.isEmpty()){
                staffDtos = mapping.toStaffDTOList(staffEntities);
            }
        }
        relatedEntities.put("fields", fieldDtos);
        relatedEntities.put("crops", cropDtos);
        relatedEntities.put("staffs", staffDtos);

        return relatedEntities;

    }

}
