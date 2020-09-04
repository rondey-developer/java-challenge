package jp.co.axa.apidemo.api;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.entities.LoginInformation;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import jp.co.axa.apidemo.repositories.LoginInformationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LoginInformationRespositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LoginInformationRepository loginInformationRepository;

    @Test
    public void whenFindByNamethenReturnLoginInformation() {
        // given
        LoginInformation tester = new LoginInformation();
        tester.setUsername("tester");
        tester.setPassword("12345");
        entityManager.persist(tester);
        entityManager.flush();

        // when
        LoginInformation found = loginInformationRepository.findByUsername(tester.getUsername());

        // then
        assertThat(found.getUsername()).isEqualTo(tester.getUsername());
    }
}
