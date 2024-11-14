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
import org.springframework.data.geo.Point;

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
            @RequestPart ("fieldLocation") String fieldLocation, // As a string (JSON string) -> Point
            @RequestPart ("fieldExtentsize") String fieldExtentsize, // As a string -> double
            @RequestPart ("fieldImage1") MultipartFile fieldImage1,
            @RequestPart ("fieldImage2") MultipartFile fieldImage2,
            @RequestPart ("staffIds") String staffIds // As a string (JSON Array) -> List
    ) {

        //System.out.println("Received request with Content-Type: multipart/form-data");
        //System.out.println("staffIds: " + staffIds);

        // image1, image2 ----> Base64
        String base64Image1 = "";
        String base64Image2 = "";

        try {

            // Convert fieldLocation JSON string to Point
            Point pointFieldLocation = AppUtil.convertToPoint(fieldLocation);

            // Convert staffIds JSON array string to List<String>
            List<String> staffIdList = AppUtil.convertJsonArrayToList(staffIds);
            //System.out.println("staffIdList: " + staffIdList);

            byte[] bytesImage1 = fieldImage1.getBytes();
            byte[] bytesImage2 = fieldImage2.getBytes();

            base64Image1 = AppUtil.convertImageToBase64(bytesImage1);
            base64Image2 = AppUtil.convertImageToBase64(bytesImage2);

            // field code generate
            String fieldCode = AppUtil.generateCode("FIELD");

            // build the object
            FieldDTO fieldDTO = new FieldDTO();

            fieldDTO.setFieldCode(fieldCode);
            fieldDTO.setFieldName(fieldName);
            fieldDTO.setFieldLocation(pointFieldLocation);
            fieldDTO.setFieldExtentsize(Double.parseDouble(fieldExtentsize)); // convert Sting to double
            fieldDTO.setFieldImage1(base64Image1);
            fieldDTO.setFieldImage2(base64Image2);
            fieldDTO.setStaffIds(staffIdList); // convert String to List<String>

            //System.out.println("fieldDTO: " + fieldDTO);

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
            return new SelectedErrorStatus(1, "Field Code is not valid");
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{fieldCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateField(
            @RequestPart ("fieldName") String fieldName,
            @RequestPart ("fieldLocation") String fieldLocation, // As a string (JSON string) -> Point
            @RequestPart ("fieldExtentsize") String fieldExtentsize, // As a string -> double
            @RequestPart ("fieldImage1") MultipartFile fieldImage1,
            @RequestPart ("fieldImage2") MultipartFile fieldImage2,
            @RequestPart ("staffIds") String staffIds, // As a string (JSON Array) -> Set
            @PathVariable ("fieldCode") String fieldCode
    ) {

        // image1, image2 ----> Base64
        String base64Image1 = "";
        String base64Image2 = "";

        try {

            // Convert fieldLocation JSON string to Point
            Point pointFieldLocation = AppUtil.convertToPoint(fieldLocation);

            // Convert staffIds JSON array string to Set<String>
            List<String> staffIdList = AppUtil.convertJsonArrayToList(staffIds);

            byte[] bytesImage1 = fieldImage1.getBytes();
            byte[] bytesImage2 = fieldImage2.getBytes();

            base64Image1 = AppUtil.convertImageToBase64(bytesImage1);
            base64Image2 = AppUtil.convertImageToBase64(bytesImage2);

            // build the object
            FieldDTO fieldDTO = new FieldDTO();

            fieldDTO.setFieldCode(fieldCode);
            fieldDTO.setFieldName(fieldName);
            fieldDTO.setFieldLocation(pointFieldLocation);
            fieldDTO.setFieldExtentsize(Double.parseDouble(fieldExtentsize)); // convert Sting to double
            fieldDTO.setFieldImage1(base64Image1);
            fieldDTO.setFieldImage2(base64Image2);
            fieldDTO.setStaffIds(staffIdList); // convert String to List<String>

            fieldService.updateField(fieldCode, fieldDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
