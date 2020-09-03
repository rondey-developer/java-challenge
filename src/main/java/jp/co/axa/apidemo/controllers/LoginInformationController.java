package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.LoginInformation;
import jp.co.axa.apidemo.repositories.LoginInformationRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class LoginInformationController {
    private LoginInformationRepository loginInformationRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginInformationController(LoginInformationRepository loginInformationRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.loginInformationRepository = loginInformationRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/register")
    public void signUp(@RequestBody LoginInformation user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        loginInformationRepository.save(user);
    }
}
