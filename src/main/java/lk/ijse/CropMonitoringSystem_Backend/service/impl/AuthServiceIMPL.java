package lk.ijse.CropMonitoringSystem_Backend.service.impl;

import lk.ijse.CropMonitoringSystem_Backend.customExceptions.UserNotFoundException;
import lk.ijse.CropMonitoringSystem_Backend.dao.UserDAO;
import lk.ijse.CropMonitoringSystem_Backend.dto.impl.UserDTO;
import lk.ijse.CropMonitoringSystem_Backend.entity.impl.UserEntity;
import lk.ijse.CropMonitoringSystem_Backend.secure.JWTAuthResponse;
import lk.ijse.CropMonitoringSystem_Backend.secure.SignIn;
import lk.ijse.CropMonitoringSystem_Backend.service.AuthService;
import lk.ijse.CropMonitoringSystem_Backend.service.JWTService;
import lk.ijse.CropMonitoringSystem_Backend.util.AppUtil;
import lk.ijse.CropMonitoringSystem_Backend.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceIMPL implements AuthService {

    private final UserDAO userDAO;
    private final Mapping mapping;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    // user log in
    @Override
    public JWTAuthResponse signIn(SignIn signIn) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signIn.getEmail(),signIn.getPassword())); // get email and pw when user log in
        var user = userDAO.findByEmail(signIn.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User Not found"));
        var generatedToken = jwtService.generateToken(user);

        return JWTAuthResponse.builder().token(generatedToken).build();
    }

    // save user in database and issue a token
    @Override
    public JWTAuthResponse signUp(UserDTO userDTO) {
        userDTO.setUserId(AppUtil.generateCode("USER"));
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // save user
        UserEntity savedUser = userDAO.save(mapping.toUserEntity(userDTO));
        // generate a token and return it
        var token = jwtService.generateToken(savedUser);

        return JWTAuthResponse.builder().token(token).build();
    }

    // refresh token
    @Override
    public JWTAuthResponse refreshToken(String accessToken) {
        //extract username from existing token
        var userName = jwtService.extractUserName(accessToken);
        //check the user availability in the db
        var findUser=  userDAO.findByEmail(userName)
                .orElseThrow(() -> new UserNotFoundException("User Not found"));
        var refreshedToken = jwtService.refreshToken(findUser);

        return JWTAuthResponse.builder().token(refreshedToken).build();
    }
}
