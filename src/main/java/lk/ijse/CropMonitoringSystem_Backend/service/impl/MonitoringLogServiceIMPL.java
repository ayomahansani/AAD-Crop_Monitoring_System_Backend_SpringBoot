package lk.ijse.CropMonitoringSystem_Backend.service.impl;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.MonitoringLogNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dao.MonitoringLogDAO;
import lk.ijse.CropMonitoringSystem_Backend.dto.MonitoringLogStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.MonitoringLogDTO;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.MonitoringLogEntity;
import lk.ijse.CropMonitoringSystem_Backend.service.MonitoringLogService;
import lk.ijse.CropMonitoringSystem_Backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MonitoringLogServiceIMPL implements MonitoringLogService {

    @Autowired
    private MonitoringLogDAO monitoringLogDAO;

    @Autowired
    private Mapping mapping;


    // save log
    @Override
    public void saveMonitoringLog(MonitoringLogDTO monitoringLogDTO) {
        MonitoringLogEntity savedLog = monitoringLogDAO.save(mapping.toMonitoringLogEntity(monitoringLogDTO));
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
