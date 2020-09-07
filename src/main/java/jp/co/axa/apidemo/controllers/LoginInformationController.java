package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.Exception.LoginInformationExistException;
import jp.co.axa.apidemo.entities.LoginInformation;
import jp.co.axa.apidemo.repositories.LoginInformationRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1")
public class LoginInformationController {
    private LoginInformationRepository loginInformationRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public LoginInformationController(LoginInformationRepository loginInformationRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.loginInformationRepository = loginInformationRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/register")
    public void signUp(@RequestBody LoginInformation user) {
        if(user != null) {
            String username = user.getUsername();
            if(!userExist(username)) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                loginInformationRepository.save(user);
                logger.info("user registered successfully");
            }
            else {
                logger.info("user registered unsuccessfully ");
                throw new LoginInformationExistException();
            }
        }
    }

    private Boolean userExist(String username){
        LoginInformation registeredUser = loginInformationRepository.findByUsername(username);
        return registeredUser != null;
    }
}
