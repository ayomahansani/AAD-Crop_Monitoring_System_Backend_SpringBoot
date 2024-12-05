package lk.ijse.CropMonitoringSystem_Backend.controller;

import lk.ijse.CropMonitoringSystem_Backend.dto.impl.StaffDTO;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.UserDTO;
import lk.ijse.CropMonitoringSystem_Backend.secure.JWTAuthResponse;
import lk.ijse.CropMonitoringSystem_Backend.secure.SignIn;
import lk.ijse.CropMonitoringSystem_Backend.service.AuthService;
import lk.ijse.CropMonitoringSystem_Backend.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final StaffService staffService;
    private final AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    // user register
    @PostMapping(value = "signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATOR') or hasRole('SCIENTIST')")
    public ResponseEntity<JWTAuthResponse> createUser(@RequestBody UserDTO userDTO) {

        try {

            logger.info("Request received to save user: {}", userDTO);

            // Check if a staff member exists with the given email
            Optional<StaffDTO> existingStaff = staffService.findByEmail(userDTO.getEmail());

            if (!existingStaff.isPresent()) {

                // Save new staff member if none exists
                StaffDTO newStaffDto = new StaffDTO();

                newStaffDto.setEmail(userDTO.getEmail());
                newStaffDto.setRole(userDTO.getRole());

                // get saved staff dto
                StaffDTO staffDTO = staffService.saveStaff(newStaffDto);

                // Set the saved staff ID to the user DTO
                userDTO.setStaffId(staffDTO.getStaffId());
            } else {
                // Link to the existing staff member
                userDTO.setStaffId(existingStaff.get().getStaffId());
            }

            // Save the user
            return ResponseEntity.ok(authService.signUp(userDTO)); // don't pass direct to the database.Pass to auth service

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error saving user.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // log in user
    @PostMapping(value = "signIn", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JWTAuthResponse> signIn(@RequestBody SignIn signIn){
        logger.info("Request received to user sign in.");
        return ResponseEntity.ok(authService.signIn(signIn));
    }

    // refresh token
    @PostMapping("refresh")
    public ResponseEntity<JWTAuthResponse> refreshToken(@RequestParam("existingToken") String existingToken) {
        logger.info("Request received to refresh token.");
        return ResponseEntity.ok(authService.refreshToken(existingToken));
    }

}
