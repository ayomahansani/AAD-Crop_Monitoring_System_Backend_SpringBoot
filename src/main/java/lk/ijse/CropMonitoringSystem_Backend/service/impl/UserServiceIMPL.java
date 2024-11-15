package lk.ijse.CropMonitoringSystem_Backend.service.impl;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.UserNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.dao.UserDAO;
import lk.ijse.CropMonitoringSystem_Backend.dto.UserStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.UserDTO;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.UserEntity;
import lk.ijse.CropMonitoringSystem_Backend.service.UserService;
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


    @Override
    public void saveUser(UserDTO userDTO) {

    }

    @Override
    public UserStatus getSelectedUser(String userId) {
        return null;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return List.of();
    }

    @Override
    public void deleteUser(String userId) {

    }

    @Override
    public void updateUser(String userId, UserDTO updatedUserDTO) {

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
