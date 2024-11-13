package lk.ijse.CropMonitoringSystem_Backend.controller;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.CropNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.CropStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.CropDTO;
import lk.ijse.CropMonitoringSystem_Backend.service.CropService;
import lk.ijse.CropMonitoringSystem_Backend.util.AppUtil;
import lk.ijse.CropMonitoringSystem_Backend.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/crops")
@CrossOrigin
public class CropController {

    @Autowired
    private CropService cropService;


    // save crop
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveCrop(
            @RequestPart ("cropCommonName") String cropCommonName,
            @RequestPart ("cropScientificName") String cropScientificName,
            @RequestPart ("cropCategory") String cropCategory,
            @RequestPart ("cropSeason") String cropSeason,
            @RequestPart ("cropImage") MultipartFile cropImage
    ) {

        // cropImage ----> Base64
        String base64CropImage = "";

        try {

            byte[] cropImageBytes = cropImage.getBytes();
            base64CropImage = AppUtil.convertImageToBase64(cropImageBytes);

            // crop code generate
            String cropCode = AppUtil.generateCode("CROP");

            // build the object
            CropDTO cropDTO = new CropDTO();

            cropDTO.setCropCode(cropCode);
            cropDTO.setCropCommonName(cropCommonName);
            cropDTO.setCropScientificName(cropScientificName);
            cropDTO.setCropCategory(cropCategory);
            cropDTO.setCropSeason(cropSeason);
            cropDTO.setCropImage(base64CropImage);

            cropService.saveCrop(cropDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // get selected crop
    @GetMapping(value = "/{cropCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CropStatus getSelectedCrop(@PathVariable ("cropCode") String cropCode) {
        if(!Regex.codeMatcher(cropCode)) {
            return new SelectedErrorStatus(1, "Crop code is not valid");
        }
        return cropService.getSelectedCrop(cropCode);
    }


    // get all crops
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CropDTO> getAllCrops() {
        return cropService.getAllCrops();
    }


    // delete crop
    @DeleteMapping(value = "/{cropCode}")
    public ResponseEntity<Void> deleteCrop(@PathVariable ("cropCode") String cropCode) {
        try {
            if(!Regex.codeMatcher(cropCode)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            cropService.deleteCrop(cropCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CropNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // update crop
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{cropCode}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateCrop(
            @RequestPart ("cropCommonName") String cropCommonName,
            @RequestPart ("cropScientificName") String cropScientificName,
            @RequestPart ("cropCategory") String cropCategory,
            @RequestPart ("cropSeason") String cropSeason,
            @RequestPart ("cropImage") MultipartFile cropImage,
            @PathVariable ("cropCode") String cropCode
    ) {

        // cropImage ----> Base64
        String base64CropImage = "";

        try {

            byte[] cropImageBytes = cropImage.getBytes();
            base64CropImage = AppUtil.convertImageToBase64(cropImageBytes);

            // build the object
            CropDTO cropDTO = new CropDTO();

            cropDTO.setCropCode(cropCode);
            cropDTO.setCropCommonName(cropCommonName);
            cropDTO.setCropScientificName(cropScientificName);
            cropDTO.setCropCategory(cropCategory);
            cropDTO.setCropSeason(cropSeason);
            cropDTO.setCropImage(base64CropImage);

            cropService.updateCrop(cropCode, cropDTO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
