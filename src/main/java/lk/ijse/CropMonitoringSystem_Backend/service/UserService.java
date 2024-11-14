package lk.ijse.CropMonitoringSystem_Backend.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    UserDetailsService userDetailsService(); // can fetch a user easily from the database

}
