package lk.ijse.CropMonitoringSystem_Backend.controller;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.EquipmentNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.EquipmentStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.EquipmentDTO;
import lk.ijse.CropMonitoringSystem_Backend.service.EquipmentService;
import lk.ijse.CropMonitoringSystem_Backend.util.Regex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/equipments")
@CrossOrigin
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    private static final Logger logger = LoggerFactory.getLogger(EquipmentController.class);


    // save equipment
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        try {
            logger.info("Request received to save equipment: {}", equipmentDTO);
            equipmentService.saveEquipment(equipmentDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error saving equipment.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // get selected equipment
    @GetMapping(value = "/{equipmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EquipmentStatus getSelectedEquipment(@PathVariable ("equipmentId") String equipmentId){
        logger.info("Request received to retrieve a equipment with id: {}", equipmentId);
        if (!Regex.codeMatcher(equipmentId)) {
            return new SelectedErrorStatus(1, "Equipment ID is not valid");
        }
        return equipmentService.getSelectedEquipment(equipmentId);
    }


    // get all equipments
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EquipmentDTO> getAllEquipments(){
        logger.info("Request received to retrieve all equipments.");
        return equipmentService.getAllEquipments();
    }


    // delete equipment
    @DeleteMapping(value = "/{equipmentId}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable ("equipmentId") String equipmentId) {
        try {
            logger.info("Request received to delete equipment with id: {}", equipmentId);
            if (!Regex.codeMatcher(equipmentId)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            equipmentService.deleteEquipment(equipmentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EquipmentNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error deleting equipment with id: {}", equipmentId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // update equipment
    @PutMapping(value = "/{equipmentId}")
    public ResponseEntity<Void> updateEquipment(@PathVariable ("equipmentId") String equipmentId, @RequestBody EquipmentDTO updatedEquipmentDTO) {
        try {
            logger.info("Request received to update equipment with id: {}, Data: {}", equipmentId, updatedEquipmentDTO);
            if (!Regex.codeMatcher(equipmentId) || updatedEquipmentDTO == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            equipmentService.updateEquipment(equipmentId, updatedEquipmentDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EquipmentNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error updating equipment with id: {}", equipmentId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // get equipment related to a staff
    @GetMapping(value = "/staff/{staffId}")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentByStaffId(@PathVariable ("staffId") String staffId) {
        List<EquipmentDTO> equipmentDTOList = equipmentService.getEquipmentByStaffId(staffId);
        return ResponseEntity.ok(equipmentDTOList);
    }


    // get equipment related to a field
    @GetMapping(value = "/field/{fieldCode}")
    public ResponseEntity<List<EquipmentDTO>> getEquipmentByFieldCode(@PathVariable ("fieldCode") String fieldCode) {
        List<EquipmentDTO> equipmentDTOList = equipmentService.getEquipmentByFieldId(fieldCode);
        return ResponseEntity.ok(equipmentDTOList);
    }


}
