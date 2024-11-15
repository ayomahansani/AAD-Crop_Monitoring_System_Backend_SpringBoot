package lk.ijse.CropMonitoringSystem_Backend.controller;

import lk.ijse.CropMonitoringSystem_Backend.dto.impl.UserDTO;
import lk.ijse.CropMonitoringSystem_Backend.service.UserService;
import lk.ijse.CropMonitoringSystem_Backend.util.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;


    // get all users
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }


    // delete user
    @DeleteMapping(value = "/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable ("email") String email){

        // validate email
        if(!Regex.emailValidator(email)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // fetch user by email
        Optional<UserDTO> userDtoOptionalByEmail = userService.findByEmail(email);
        if(userDtoOptionalByEmail.isPresent()) {
            // get user's id
            String userId = userDtoOptionalByEmail.get().getUserId();
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // update user
    @PutMapping(value = "/{email}")
    public ResponseEntity<Void> updateUser(@PathVariable ("email") String email, @RequestBody UserDTO updatedUserDTO){

        // validate email
        if(!Regex.emailValidator(email) || updatedUserDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // fetch user by email
        Optional<UserDTO> userDtoOptionalByEmail = userService.findByEmail(email);
        if(userDtoOptionalByEmail.isPresent()) {
            // get user's id
            String userId = userDtoOptionalByEmail.get().getUserId();
            userService.updateUser(userId, updatedUserDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
