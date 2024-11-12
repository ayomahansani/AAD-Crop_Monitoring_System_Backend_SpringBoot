package lk.ijse.CropMonitoringSystem_Backend.controller;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.FieldNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.FieldStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.FieldDTO;
import lk.ijse.CropMonitoringSystem_Backend.service.FieldService;
import lk.ijse.CropMonitoringSystem_Backend.util.AppUtil;
import lk.ijse.CropMonitoringSystem_Backend.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/fields")
@CrossOrigin
public class FieldController {

    @Autowired
    private FieldService fieldService;

    // save field
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveField(
            @RequestPart ("fieldName") String fieldName,
            @RequestPart ("fieldLocation") Point fieldLocation,
            @RequestPart ("fieldExtentsize") double fieldExtentsize,
            @RequestPart ("fieldImage1") MultipartFile fieldImage1,
            @RequestPart ("fieldImage2") MultipartFile fieldImage2
    ) {

        // image1, image2 ----> Base64
        String base64Image1 = "";
        String base64Image2 = "";

        try {

            byte[] bytesImage1 = fieldImage1.getBytes();
            byte[] bytesImage2 = fieldImage2.getBytes();

            base64Image1 = AppUtil.convertImageToBase64(bytesImage1);
            base64Image2 = AppUtil.convertImageToBase64(bytesImage2);

            // field id generate
            String fieldCode = AppUtil.generateCode("FIELD");

            // build the object
            FieldDTO fieldDTO = new FieldDTO();

            fieldDTO.setFieldCode(fieldCode);
            fieldDTO.setFieldName(fieldName);
            fieldDTO.setFieldLocation(fieldLocation);
            fieldDTO.setFieldExtentsize(fieldExtentsize);
            fieldDTO.setFieldImage1(base64Image1);
            fieldDTO.setFieldImage2(base64Image2);

            fieldService.saveField(fieldDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    // get selected field
    @GetMapping(value = "/{fieldCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public FieldStatus getSelectedField(@PathVariable ("fieldCode") String fieldCode) {
        if(!Regex.codeMatcher(fieldCode)){
            return new SelectedErrorStatus(1, "FieldCode is not valid");
        }
        return fieldService.getSelectedField(fieldCode);
    }


    // get all fields
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FieldDTO> getAllFields() {
        return fieldService.getAllFields();
    }


    // delete field
    @DeleteMapping(value = "/{fieldCode}")
    public ResponseEntity<Void> deleteField(@PathVariable("fieldCode") String fieldCode) {
        try {
            if(!Regex.codeMatcher(fieldCode)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            fieldService.deleteField(fieldCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (FieldNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // update field
    @PutMapping(value = "/{fieldCode}")
    public ResponseEntity<FieldDTO> updateField(@PathVariable("fieldCode") String fieldCode, @RequestBody FieldDTO updatedFieldDTO) {
        try {
            if(!Regex.codeMatcher(fieldCode) || updatedFieldDTO == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            fieldService.updateField(fieldCode, updatedFieldDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (FieldNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
