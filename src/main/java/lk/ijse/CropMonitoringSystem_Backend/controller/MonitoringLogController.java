package lk.ijse.CropMonitoringSystem_Backend.controller;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.MonitoringLogNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.MonitoringLogStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.MonitoringLogDTO;
import lk.ijse.CropMonitoringSystem_Backend.service.MonitoringLogService;
import lk.ijse.CropMonitoringSystem_Backend.util.AppUtil;
import lk.ijse.CropMonitoringSystem_Backend.util.Regex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/logs")
@CrossOrigin
public class MonitoringLogController {

    @Autowired
    private MonitoringLogService monitoringLogService;

    private static final Logger logger = LoggerFactory.getLogger(MonitoringLogController.class);


    // save log
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveMonitoringLog(
            @RequestPart ("logDate") String logDate, // As a string -> LocalDate
            @RequestPart ("logDetails") String logDetails,
            @RequestPart ("observedImage") MultipartFile observedImage,
            @RequestPart ("fieldCodes") String fieldCodes, // As a string (JSON Array) -> List
            @RequestPart ("cropCodes") String cropCodes, // As a string (JSON Array) -> List
            @RequestPart ("staffIds") String staffIds // As a string (JSON Array) -> List
    ) {

        // observedImage ----> Base64
        String base64ObservedImage = "";

        try {

            logger.info("Request received to save log.");

            // Convert fieldCodes,cropCodes,staffIds (JSON array string) to List<String>
            List<String> fieldCodeList = AppUtil.convertJsonArrayToList(fieldCodes);
            List<String> cropCodeList = AppUtil.convertJsonArrayToList(cropCodes);
            List<String> staffIdList = AppUtil.convertJsonArrayToList(staffIds);

            byte[] observedImageBytes = observedImage.getBytes();
            base64ObservedImage = AppUtil.convertImageToBase64(observedImageBytes);

            // log code generate
            String logCode = AppUtil.generateCode("LOG");

            // build the object
            MonitoringLogDTO monitoringLogDTO = new MonitoringLogDTO();

            monitoringLogDTO.setLogCode(logCode);
            monitoringLogDTO.setLogDate(LocalDate.parse(logDate)); // convert Sting to LocalDate
            monitoringLogDTO.setLogDetails(logDetails);
            monitoringLogDTO.setObservedImage(base64ObservedImage);
            monitoringLogDTO.setFieldCodes(fieldCodeList); // convert String to List<String>
            monitoringLogDTO.setCropCodes(cropCodeList); // convert String to List<String>
            monitoringLogDTO.setStaffIds(staffIdList); // convert String to List<String>

            monitoringLogService.saveMonitoringLog(monitoringLogDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error saving log.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    // get selected log
    @GetMapping(value = "/{logCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MonitoringLogStatus getSelectedMonitoringLog(@PathVariable ("logCode") String logCode) {
        logger.info("Request received to retrieve a log with code: {}", logCode);
        if(!Regex.codeMatcher(logCode)) {
            return new SelectedErrorStatus(1, "Log Code is not valid");
        }
        return monitoringLogService.getSelectedMonitoringLog(logCode);
    }


    // get all logs
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonitoringLogDTO> getAllMonitoringLogs() {
        logger.info("Request received to retrieve all logs.");
        return monitoringLogService.getAllMonitoringLogs();
    }


    // delete log
    @DeleteMapping(value = "/{logCode}")
    public ResponseEntity<Void> deleteMonitoringLog(@PathVariable ("logCode") String logCode) {
        try {
            logger.info("Request received to delete log with code: {}", logCode);
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
            logger.error("Error deleting log with code: {}", logCode, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // update log
    @PutMapping(value = "/{logCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateMonitoringLog(
            @RequestPart ("logDate") String logDate, // As a string -> LocalDate
            @RequestPart ("logDetails") String logDetails,
            @RequestPart ("observedImage") MultipartFile observedImage,
            @RequestPart ("fieldCodes") String fieldCodes, // As a string (JSON Array) -> List
            @RequestPart ("cropCodes") String cropCodes, // As a string (JSON Array) -> List
            @RequestPart ("staffIds") String staffIds, // As a string (JSON Array) -> List
            @PathVariable ("logCode") String logCode
    ) {

        // observedImage ----> Base64
        String base64ObservedImage = "";

        try {

            logger.info("Request received to update log with code: {}", logCode);

            // Convert fieldCodes,cropCodes,staffIds (JSON array string) to List<String>
            List<String> fieldCodeList = AppUtil.convertJsonArrayToList(fieldCodes);
            List<String> cropCodeList = AppUtil.convertJsonArrayToList(cropCodes);
            List<String> staffIdList = AppUtil.convertJsonArrayToList(staffIds);

            byte[] observedImageBytes = observedImage.getBytes();
            base64ObservedImage = AppUtil.convertImageToBase64(observedImageBytes);

            // build the object
            MonitoringLogDTO monitoringLogDTO = new MonitoringLogDTO();

            monitoringLogDTO.setLogCode(logCode);
            monitoringLogDTO.setLogDate(LocalDate.parse(logDate)); // convert Sting to LocalDate
            monitoringLogDTO.setLogDetails(logDetails);
            monitoringLogDTO.setObservedImage(base64ObservedImage);
            monitoringLogDTO.setFieldCodes(fieldCodeList); // convert String to List<String>
            monitoringLogDTO.setCropCodes(cropCodeList); // convert String to List<String>
            monitoringLogDTO.setStaffIds(staffIdList); // convert String to List<String>

            monitoringLogService.updateMonitoringLog(logCode, monitoringLogDTO);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error updating log with code: {}", logCode, e);
        }

    }


    // get related fields, crops, staffs
    @GetMapping("/{logCode}/related-entities")
    public ResponseEntity<Map<String, Object>> getRelatedEntities(@PathVariable String logCode) {
        Map<String, Object> relatedEntities = monitoringLogService.getRelatedEntitiesAsDtos(logCode);
        return ResponseEntity.ok(relatedEntities);
    }

}
