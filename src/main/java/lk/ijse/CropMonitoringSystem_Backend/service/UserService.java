package lk.ijse.CropMonitoringSystem_Backend.service;

import lk.ijse.CropMonitoringSystem_Backend.dto.UserStatus;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(UserDTO userDTO);
    UserStatus getSelectedUser(String userId);
    List<UserDTO> getAllUsers();
    void deleteUser(String userId);
    void updateUser(String userId, UserDTO updatedUserDTO);

    Optional<UserDTO> findByEmail(String email);
    UserDetailsService userDetailsService(); // can fetch a user easily from the database
}
