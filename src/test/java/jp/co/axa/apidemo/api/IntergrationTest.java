package jp.co.axa.apidemo.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.entities.LoginInformation;
import jp.co.axa.apidemo.repositories.LoginInformationRepository;
import jp.co.axa.apidemo.security.SecurityConstants;
import jp.co.axa.apidemo.services.EmployeeService;
import jp.co.axa.apidemo.services.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class IntergrationTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private LoginInformationRepository loginInformationRepository;

    @MockBean
    private UserServiceImpl service;

    @MockBean
    private EmployeeService employeeService;

    private Employee tester = new Employee();

    private LoginInformation existingAdmin = new LoginInformation();

    @Before
    public void setupMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    //Setting for retrieve Employee process
    @Before
    public void setupEmployee(){
        tester.setName("tester");
        tester.setSalary(200);
        tester.setDepartment("QA department");
        List<Employee> allEmployee = Arrays.asList(tester);
        given(employeeService.retrieveEmployees()).willReturn(allEmployee);
    }

    //Setup a registered admin
    @Before
    public  void setupAdmin(){
        existingAdmin.setUsername("admin");
        existingAdmin.setPassword("admin");
        existingAdmin.setPassword(bCryptPasswordEncoder.encode(existingAdmin.getPassword()));
        given(service.loadUserByUsername(existingAdmin.getUsername()))
                .willReturn(new User(existingAdmin.getUsername(), existingAdmin.getPassword(), Collections.emptyList()));
    }

    /*Scenario1 :
    Given:
        an register admin user is going to login in
    When:
        the user is to request to get all the employee with token got from login process
    Then:
        the user can retrieve all the employee
     */
    @Test
    public void scenario1() throws Exception {
        //given a loginInformation with same username and password with register user
        LoginInformation registeredAdmin = new LoginInformation();
        registeredAdmin.setUsername("admin");
        registeredAdmin.setPassword("admin");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(registeredAdmin);

        //Login process
        MvcResult mvcResult = mvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        //Response with JWT token
        String token = mvcResult.getResponse().getHeader(SecurityConstants.HEADER_NAME);

        /*When:
            send a get request wtih token got from login to get a list of employees
         Then:
            the user can retrieve all the employee
        */
        mvc.perform(get("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .header(SecurityConstants.HEADER_NAME, SecurityConstants.TOKEN_PREFIX+ token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(tester.getName())));
    }
}
