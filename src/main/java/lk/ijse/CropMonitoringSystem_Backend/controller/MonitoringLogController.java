package lk.ijse.CropMonitoringSystem_Backend.controller;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.MonitoringLogNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.MonitoringLogStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.MonitoringLogDTO;
import lk.ijse.CropMonitoringSystem_Backend.service.MonitoringLogService;
import lk.ijse.CropMonitoringSystem_Backend.util.AppUtil;
import lk.ijse.CropMonitoringSystem_Backend.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/logs")
@CrossOrigin
public class MonitoringLogController {

    @Autowired
    private MonitoringLogService monitoringLogService;


    // save log
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveMonitoringLog(
            @RequestPart ("logDate") LocalDate logDate,
            @RequestPart ("logDetails") String logDetails,
            @RequestPart ("observedImage") MultipartFile observedImage
    ) {

        // observedImage ----> Base64
        String base64ObservedImage = "";

        try {

            byte[] observedImageBytes = observedImage.getBytes();
            base64ObservedImage = AppUtil.convertImageToBase64(observedImageBytes);

            // log code generate
            String logCode = AppUtil.generateCode("LOG");

            // build the object
            MonitoringLogDTO monitoringLogDTO = new MonitoringLogDTO();

            monitoringLogDTO.setLogCode(logCode);
            monitoringLogDTO.setLogDate(logDate);
            monitoringLogDTO.setLogDetails(logDetails);
            monitoringLogDTO.setObservedImage(base64ObservedImage);

            monitoringLogService.saveMonitoringLog(monitoringLogDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    // get selected log
    @GetMapping(value = "/{logCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MonitoringLogStatus getSelectedMonitoringLog(@PathVariable ("logCode") String logCode) {
        if(!Regex.codeMatcher(logCode)) {
            return new SelectedErrorStatus(1, "Log Code is not valid");
        }
        return monitoringLogService.getSelectedMonitoringLog(logCode);
    }


    // get all logs
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonitoringLogDTO> getAllMonitoringLogs() {
        return monitoringLogService.getAllMonitoringLogs();
    }


    // delete log
    @DeleteMapping(value = "/{logCode}")
    public ResponseEntity<Void> deleteMonitoringLog(@PathVariable ("logCode") String logCode) {
        try {
            if(!Regex.codeMatcher(logCode)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            monitoringLogService.deleteMonitoringLog(logCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (MonitoringLogNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // update log
    @PutMapping(value = "/{logCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateMonitoringLog(
            @RequestPart ("logDate") LocalDate logDate,
            @RequestPart ("logDetails") String logDetails,
            @RequestPart ("observedImage") MultipartFile observedImage,
            @PathVariable ("logCode") String logCode
    ) {

        // observedImage ----> Base64
        String base64ObservedImage = "";

        try {

            byte[] observedImageBytes = observedImage.getBytes();
            base64ObservedImage = AppUtil.convertImageToBase64(observedImageBytes);

            // build the object
            MonitoringLogDTO monitoringLogDTO = new MonitoringLogDTO();

            monitoringLogDTO.setLogCode(logCode);
            monitoringLogDTO.setLogDate(logDate);
            monitoringLogDTO.setLogDetails(logDetails);
            monitoringLogDTO.setObservedImage(base64ObservedImage);

            monitoringLogService.updateMonitoringLog(logCode, monitoringLogDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
