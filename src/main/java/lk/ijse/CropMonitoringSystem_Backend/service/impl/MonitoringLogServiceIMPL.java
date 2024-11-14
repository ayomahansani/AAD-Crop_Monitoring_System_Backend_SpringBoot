package lk.ijse.CropMonitoringSystem_Backend.service.impl;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.*;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dao.CropDAO;
import lk.ijse.CropMonitoringSystem_Backend.dao.FieldDAO;
import lk.ijse.CropMonitoringSystem_Backend.dao.MonitoringLogDAO;
import lk.ijse.CropMonitoringSystem_Backend.dao.StaffDAO;
import lk.ijse.CropMonitoringSystem_Backend.dto.MonitoringLogStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.MonitoringLogDTO;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.CropEntity;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.FieldEntity;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.MonitoringLogEntity;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.StaffEntity;
import lk.ijse.CropMonitoringSystem_Backend.service.MonitoringLogService;
import lk.ijse.CropMonitoringSystem_Backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void saveMonitoringLog(MonitoringLogDTO monitoringLogDTO) {
        MonitoringLogEntity logEntity = mapping.toMonitoringLogEntity(monitoringLogDTO);

        // -------- Log-Field --------
        List<FieldEntity> fieldEntityList = new ArrayList<>(); // create new FieldEntity List
        List<String> fieldCodes = monitoringLogDTO.getFieldCodes(); // our dto has fieldCode List . But we want FieldEntity List
        if(fieldCodes != null){
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
        if(cropCodes != null){
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
        if(staffIds != null){
            for (String staffId : staffIds) {
                StaffEntity staffEntity = staffDAO.findById(staffId) // so get staffEntity one by one using staffIds
                        .orElseThrow(() -> new StaffNotFoundException("Staff not found with ID: " + staffId));

                staffEntityList.add(staffEntity); // add them to newly created StaffEntity List
            }
        }
        logEntity.setStaffMembers(staffEntityList); // set that StaffEntity List to the entity

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
    public void updateMonitoringLog(String logCode, MonitoringLogDTO monitoringLogDTO) {
        Optional<MonitoringLogEntity> foundLog = monitoringLogDAO.findById(logCode);
        if(!foundLog.isPresent()){
            throw new MonitoringLogNotFoundException("Log not found");
        } else {
            foundLog.get().setLogDate(monitoringLogDTO.getLogDate());
            foundLog.get().setLogDetails(monitoringLogDTO.getLogDetails());
            foundLog.get().setObservedImage(monitoringLogDTO.getObservedImage());
        }
    }

}
