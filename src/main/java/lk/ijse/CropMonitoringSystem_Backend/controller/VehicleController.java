package lk.ijse.CropMonitoringSystem_Backend.controller;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.VehicleNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.VehicleStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.VehicleDTO;
import lk.ijse.CropMonitoringSystem_Backend.service.VehicleService;
import lk.ijse.CropMonitoringSystem_Backend.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/vehicles")
@CrossOrigin
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;


    // save vehicle
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveVehicle(VehicleDTO vehicleDTO) {
        try {
            vehicleService.saveVehicle(vehicleDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // get selected vehicle
    @GetMapping(value = "/{vehicleCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleStatus getSelectedVehicle(@PathVariable ("vehicleCode") String vehicleCode) {
        if(!Regex.codeMatcher(vehicleCode)){
            return new SelectedErrorStatus(1, "Vehicle code is not valid");
        }
        return vehicleService.getSelectedVehicle(vehicleCode);
    }


    // get all vehicles
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleDTO> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }


    // delete vehicle
    @DeleteMapping(value = "/{vehicleCode}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable("vehicleCode") String vehicleCode) {
        try {
            if(!Regex.codeMatcher(vehicleCode)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            vehicleService.deleteVehicle(vehicleCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (VehicleNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // update vehicle
    @PutMapping(value = "/{vehicleCode}")
    public ResponseEntity<Void> updateVehicle(@PathVariable ("vehicleCode") String vehicleCode, @RequestBody VehicleDTO updatedVehicleDTO) {
        try {
            if(!Regex.codeMatcher(vehicleCode) || updatedVehicleDTO == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            vehicleService.updateVehicle(vehicleCode, updatedVehicleDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (VehicleNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
