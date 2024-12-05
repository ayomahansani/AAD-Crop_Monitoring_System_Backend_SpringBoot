package lk.ijse.CropMonitoringSystem_Backend.controller;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.CropNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.CropStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.CropDTO;
import lk.ijse.CropMonitoringSystem_Backend.service.CropService;
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

import java.util.List;

@RestController
@RequestMapping("api/v1/crops")
@CrossOrigin
public class CropController {

    @Autowired
    private CropService cropService;

    private static final Logger logger = LoggerFactory.getLogger(CropController.class);


    // save crop
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveCrop(
            @RequestPart ("cropCommonName") String cropCommonName,
            @RequestPart ("cropScientificName") String cropScientificName,
            @RequestPart ("cropCategory") String cropCategory,
            @RequestPart ("cropSeason") String cropSeason,
            @RequestPart ("cropImage") MultipartFile cropImage,
            @RequestPart ("fieldCode") String fieldCode
    ) {

        // cropImage ----> Base64
        String base64CropImage = "";

        try {

            logger.info("Request received to save crop.");

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
            cropDTO.setFieldCode(fieldCode);

            cropService.saveCrop(cropDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error saving crop.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // get selected crop
    @GetMapping(value = "/{cropCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CropStatus getSelectedCrop(@PathVariable ("cropCode") String cropCode) {
        logger.info("Request received to retrieve a crop with code: {}", cropCode);
        if(!Regex.codeMatcher(cropCode)) {
            return new SelectedErrorStatus(1, "Crop code is not valid");
        }
        return cropService.getSelectedCrop(cropCode);
    }


    // get all crops
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CropDTO> getAllCrops() {
        logger.info("Request received to retrieve all crops.");
        return cropService.getAllCrops();
    }


    // delete crop
    @DeleteMapping(value = "/{cropCode}")
    public ResponseEntity<Void> deleteCrop(@PathVariable ("cropCode") String cropCode) {
        try {
            logger.info("Request received to delete crop with code: {}", cropCode);
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
            logger.error("Error deleting crop with code: {}", cropCode, e);
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
            @RequestPart ("fieldCode") String fieldCode,
            @PathVariable ("cropCode") String cropCode
    ) {

        // cropImage ----> Base64
        String base64CropImage = "";

        try {

            logger.info("Request received to update crop with code: {}", cropCode);

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
            cropDTO.setFieldCode(fieldCode);

            cropService.updateCrop(cropCode, cropDTO);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error updating crop with code: {}", cropCode, e);
        }
    }


}
