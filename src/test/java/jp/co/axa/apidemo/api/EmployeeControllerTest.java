package jp.co.axa.apidemo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class EmployeeControllerTest {

    private MockMvc mvc;

    @MockBean
    private EmployeeService service;

    @Autowired
    private WebApplicationContext context;


    //Seting up for spring-boot security
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void whenNoauthenticationThenReturn403() throws Exception {
        mvc.perform(get("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    public void givenEmployeeWhenGetEmployeeThenReturnArray() throws Exception {
        Employee tester = new Employee();
        tester.setName("tester");
        tester.setSalary(200);
        tester.setDepartment("QA department");

        List<Employee> allEmployee = Arrays.asList(tester);

        given(service.retrieveEmployees()).willReturn(allEmployee);

        mvc.perform(get("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(tester.getName())));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    public void givenEmployeeWhenGetEmployeeByIdThenReturnEmployee() throws Exception {
        Employee tester = new Employee();
        tester.setName("tester");
        tester.setSalary(200);
        tester.setDepartment("QA department");

        given(service.getEmployee(1L)).willReturn(tester);

        mvc.perform(get("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(tester.getName())));
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    public void givenEmployeeWhenPostEmployeeThenReturn200() throws Exception {
        Employee tester = new Employee();
        tester.setName("tester");
        tester.setSalary(200);
        tester.setDepartment("QA department");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(tester);

        mvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();;

        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(service, times(1)).saveEmployee(employeeCaptor.capture());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    public void givenEmployeeWhenDeleteEmployeeThenReturn200() throws Exception {
        Employee tester = new Employee();
        tester.setName("tester");
        tester.setSalary(200);
        tester.setDepartment("QA department");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(tester);

        mvc.perform(delete("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

         verify(service, times(1)).deleteEmployee(1L);
    }

    @Test
    @WithMockUser(username = "admin", password = "admin")
    public void givenEmployeeWhenPutEmployeeThenReturn200() throws Exception{
        Employee tester = new Employee();
        tester.setName("tester");
        tester.setSalary(200);
        tester.setDepartment("QA department");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(tester);

        //Given that when put request is called, employee have to be found before updating
        given(service.getEmployee(1L)).willReturn(tester);

        mvc.perform(put("/api/v1/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(service, times(1)).updateEmployee(eq(1L), employeeCaptor.capture());
    }
}