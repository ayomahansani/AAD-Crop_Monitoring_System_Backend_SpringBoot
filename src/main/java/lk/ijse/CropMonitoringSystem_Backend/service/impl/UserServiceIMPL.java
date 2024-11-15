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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
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
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public List<UserDTO> getAllUsers() {
        List<UserEntity> allUsers = userDAO.findAll();
        return mapping.toUserDTOList(allUsers);
    }

    // delete user
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public void deleteUser(String userId) {
        Optional<UserEntity> foundUser = userDAO.findById(userId);
        if (!foundUser.isPresent()) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        } else {
            userDAO.deleteById(userId);
        }
    }

    // update user
    @Override
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public void updateUser(String userId, UserDTO updatedUserDTO) {
        Optional<UserEntity> foundUser = userDAO.findById(userId);
        if (foundUser.isPresent()) {
            foundUser.get().setEmail(updatedUserDTO.getEmail());
            foundUser.get().setPassword(updatedUserDTO.getPassword());
            foundUser.get().setRole(updatedUserDTO.getRole());
        }
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
