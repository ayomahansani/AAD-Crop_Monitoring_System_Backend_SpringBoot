package lk.ijse.CropMonitoringSystem_Backend.service.impl;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.CropNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.FieldNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dao.CropDAO;
import lk.ijse.CropMonitoringSystem_Backend.dao.FieldDAO;
import lk.ijse.CropMonitoringSystem_Backend.dto.CropStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.CropDTO;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.CropEntity;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.FieldEntity;
import lk.ijse.CropMonitoringSystem_Backend.service.CropService;
import lk.ijse.CropMonitoringSystem_Backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CropServiceIMPL implements CropService {

    @Autowired
    private CropDAO cropDAO;

    @Autowired
    private FieldDAO fieldDAO;

    @Autowired
    private Mapping mapping;


    // save crop
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public void saveCrop(CropDTO cropDTO) {

        String fieldCode = cropDTO.getFieldCode();
        FieldEntity fieldEntity = fieldDAO.findById(fieldCode) // get field entity using field code
                .orElseThrow(() -> new FieldNotFoundException("Field not found with ID: " + fieldCode));

        CropEntity cropEntity = mapping.toCropEntity(cropDTO);
        cropEntity.setField(fieldEntity); // set field entity to crop entity

        CropEntity savedCrop = cropDAO.save(cropEntity);

        if(savedCrop == null){
            throw new DataPersistException("Crop not saved");
        }
    }

    // get selected crop
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public CropStatus getSelectedCrop(String cropCode) {
        if(cropDAO.existsById(cropCode)) {
            CropEntity selectedCrop = cropDAO.getReferenceById(cropCode);
            return mapping.toCropDTO(selectedCrop);
        } else {
            return new SelectedErrorStatus(2, "Selected crop not found");
        }
    }

    // get all crops
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public List<CropDTO> getAllCrops() {
        List<CropEntity> cropEntityList = cropDAO.findAll();
        return mapping.toCropDTOList(cropEntityList);
    }

    // delete crop
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public void deleteCrop(String cropCode) {
        CropEntity crop = cropDAO.findById(cropCode)
                .orElseThrow(() -> new CropNotFoundException("Crop not found with code: " + cropCode));

        // Remove associations with staff members
        crop.getLogs().forEach(log -> log.getCrops().remove(crop));
        crop.getLogs().clear();

        cropDAO.deleteById(cropCode);
    }

    // update crop
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    public void updateCrop(String cropCode, CropDTO cropDTO) {
        Optional<CropEntity> foundCrop = cropDAO.findById(cropCode);
        if(!foundCrop.isPresent()){
            throw new CropNotFoundException("Crop not found");
        } else {
            // update basic crop properties
            foundCrop.get().setCropCommonName(cropDTO.getCropCommonName());
            foundCrop.get().setCropScientificName(cropDTO.getCropScientificName());
            foundCrop.get().setCropCategory(cropDTO.getCropCategory());
            foundCrop.get().setCropSeason(cropDTO.getCropSeason());

            // set the field if provided in the DTO
            String fieldCode = cropDTO.getFieldCode();
            FieldEntity fieldEntity = fieldDAO.findById(fieldCode) // get field entity using field code
                    .orElseThrow(() -> new FieldNotFoundException("Field not found with ID: " + fieldCode));

            foundCrop.get().setField(fieldEntity); // set field entity to crop entity

            // handle image
            if(cropDTO.getCropImage() != null){
                foundCrop.get().setCropImage(cropDTO.getCropImage());
            }

        }
    }

}
