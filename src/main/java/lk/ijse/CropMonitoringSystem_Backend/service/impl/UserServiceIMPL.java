package lk.ijse.CropMonitoringSystem_Backend.service.impl;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.DataPersistException;
import lk.ijse.CropMonitoringSystem_Backend.customExceptions.UserNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.customStatusCodes.SelectedErrorStatus;
import lk.ijse.CropMonitoringSystem_Backend.dao.UserDAO;
import lk.ijse.CropMonitoringSystem_Backend.dto.UserStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.UserDTO;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.UserEntity;
import lk.ijse.CropMonitoringSystem_Backend.service.UserService;
import lk.ijse.CropMonitoringSystem_Backend.util.AppUtil;
import lk.ijse.CropMonitoringSystem_Backend.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceIMPL implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private Mapping mapping;


    // save user
    @Override
    public void saveUser(UserDTO userDTO) {
        userDTO.setUserId(AppUtil.generateCode("USER"));
        UserEntity savedUser = userDAO.save(mapping.toUserEntity(userDTO));
        if (savedUser == null) {
            throw new DataPersistException("User not saved");
        }
    }

    // get selected user
    @Override
    public UserStatus getSelectedUser(String userId) {
        if(userDAO.existsById(userId)){
            UserEntity selectedUser = userDAO.getReferenceById(userId);
            return mapping.toUserDTO(selectedUser);
        } else {
            return new SelectedErrorStatus(2, "User with id " + userId + " not found");
        }
    }

    // get all users
    @Override
    public List<UserDTO> getAllUsers() {
        List<UserEntity> allUsers = userDAO.findAll();
        return mapping.toUserDTOList(allUsers);
    }

    // delete user
    @Override
    public void deleteUser(String userId) {
        userDAO.deleteById(userId);
    }

    // update user
    @Override
    public void updateUser(String userId, UserDTO updatedUserDTO) {
       userDAO.save(mapping.toUserEntity(updatedUserDTO));
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        Optional<UserEntity> userByEmail = userDAO.findByEmail(email);
        return userByEmail.map(mapping::toUserDTO);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return userName ->
                userDAO.findByEmail(userName)
                        .orElseThrow(() -> new UserNotFoundException("User not found"));

    }

}
