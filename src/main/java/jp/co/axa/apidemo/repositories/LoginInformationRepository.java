package jp.co.axa.apidemo.repositories;

import jp.co.axa.apidemo.entities.LoginInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginInformationRepository extends JpaRepository<LoginInformation, Long> {
    LoginInformation findByUsername(String username);
}
