package lk.ijse.CropMonitoringSystem_Backend.service.impl;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.CropNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dao.CropDAO;
import lk.ijse.CropMonitoringSystem_Backend.dto.CropStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.CropDTO;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.CropEntity;
import lk.ijse.CropMonitoringSystem_Backend.service.CropService;
import lk.ijse.CropMonitoringSystem_Backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Mapping mapping;


    @Override
    public void saveCrop(CropDTO cropDTO) {
        CropEntity savedCrop = cropDAO.save(mapping.toCropEntity(cropDTO));
        if(savedCrop == null){
            throw new DataPersistException("Crop not saved");
        }
    }

    @Override
    public CropStatus getSelectedCrop(String cropCode) {
        if(cropDAO.existsById(cropCode)) {
            CropEntity selectedCrop = cropDAO.getReferenceById(cropCode);
            return mapping.toCropDTO(selectedCrop);
        } else {
            return new SelectedErrorStatus(2, "Selected crop not found");
        }
    }

    @Override
    public List<CropDTO> getAllCrops() {
        List<CropEntity> cropEntityList = cropDAO.findAll();
        return mapping.toCropDTOList(cropEntityList);
    }

    @Override
    public void deleteCrop(String cropCode) {
        Optional<CropEntity> foundCrop = cropDAO.findById(cropCode);
        if(!foundCrop.isPresent()){
            throw new CropNotFoundException("Crop not found");
        } else {
            cropDAO.deleteById(cropCode);
        }
    }
}
