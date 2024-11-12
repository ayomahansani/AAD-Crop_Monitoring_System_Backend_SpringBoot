package lk.ijse.CropMonitoringSystem_Backend.controller;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.StaffNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dao.StaffDAO;
import lk.ijse.CropMonitoringSystem_Backend.dto.StaffStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.StaffDTO;
import lk.ijse.CropMonitoringSystem_Backend.service.StaffService;
import lk.ijse.CropMonitoringSystem_Backend.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/staffs")
@CrossOrigin
public class StaffController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private StaffDAO staffDAO;

    // save staff
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveStaff(@RequestBody StaffDTO staffDTO) {
        try {
            staffService.saveStaff(staffDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // get selected staff
    @GetMapping(value = "/{staffId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StaffStatus getSelectedStaff(@PathVariable ("staffId") String staffId) {
        if(!Regex.codeMatcher(staffId)){
            return new SelectedErrorStatus(1, "Staff ID is not valid");
        }
        return staffService.getSelectedStaff(staffId);
    }


    // get all staffs
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StaffDTO> getAllStaffs() {
        return staffService.getAllStaffs();
    }


    // delete staff
    @DeleteMapping(value = "/{staffId}")
    public ResponseEntity<Void> deleteStaff(@PathVariable ("staffId") String staffId) {
        try {
            if(!Regex.codeMatcher(staffId)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            staffService.deleteStaff(staffId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (StaffNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // update staff
    @PutMapping(value = "/{staffId}")
    public ResponseEntity<Void> updateStaff(@PathVariable ("staffId") String staffId, @RequestBody StaffDTO updatedStaffDTO) {
        try {
            if(!Regex.codeMatcher(staffId) || updatedStaffDTO == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            staffService.updateStaff(staffId, updatedStaffDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (StaffNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
