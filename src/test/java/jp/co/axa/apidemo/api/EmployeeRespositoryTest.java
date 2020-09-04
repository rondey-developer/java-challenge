package jp.co.axa.apidemo.api;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
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
public class EmployeeRespositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void whenFindByName_thenReturnListOfEmployee() {
        // given
        Employee tester = new Employee();
        tester.setName("tester");
        entityManager.persist(tester);
        entityManager.flush();

        // when
        List<Employee> found = employeeRepository.findByName(tester.getName());

        // then
        assertThat(found.get(0).getName()).isEqualTo(tester.getName());
    }

}
