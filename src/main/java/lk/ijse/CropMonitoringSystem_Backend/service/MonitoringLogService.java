package lk.ijse.CropMonitoringSystem_Backend.service;

import lk.ijse.CropMonitoringSystem_Backend.dto.MonitoringLogStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.MonitoringLogDTO;

import java.util.List;

public interface MonitoringLogService {
    void saveMonitoringLog(MonitoringLogDTO monitoringLogDTO);

    MonitoringLogStatus getSelectedMonitoringLog(String logCode);

    List<MonitoringLogDTO> getAllMonitoringLogs();

    void deleteMonitoringLog(String logCode);

    void updateMonitoringLog(String logCode, MonitoringLogDTO monitoringLogDTO);
}
