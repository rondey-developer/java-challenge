package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.LoginInformation;
import jp.co.axa.apidemo.repositories.LoginInformationRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class LoginInformationServiceImpl implements UserDetailsService {
    private LoginInformationRepository loginInformationRepository;

    public LoginInformationServiceImpl(LoginInformationRepository loginInformationRepository) {
        this.loginInformationRepository = loginInformationRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginInformation loginInformation = loginInformationRepository.findByUsername(username);
        if(loginInformation == null){
            throw new UsernameNotFoundException(username);
        }
        return new User(loginInformation.getUsername(), loginInformation.getPassword(), Collections.emptyList());
    }
}
