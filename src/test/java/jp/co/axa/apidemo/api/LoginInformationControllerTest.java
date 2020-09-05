package jp.co.axa.apidemo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.entities.LoginInformation;
import jp.co.axa.apidemo.repositories.LoginInformationRepository;
import jp.co.axa.apidemo.security.AuthenticationFilter;
import jp.co.axa.apidemo.security.SecurityConstants;
import jp.co.axa.apidemo.services.UserServiceImpl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class LoginInformationControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private LoginInformationRepository loginInformationRepository;

    @MockBean
    private UserServiceImpl service;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthenticationFilter authenticationFilter;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void adminUserCanRegister() throws Exception {
        LoginInformation admin = new LoginInformation();
        admin.setUsername("admin");
        admin.setPassword("admin");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(admin);

        mvc.perform(post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void adminUserCannotRegisterWithExistingUsername() throws Exception {
        LoginInformation existingAdmin = new LoginInformation();
        existingAdmin.setUsername("admin");
        existingAdmin.setPassword("admin");

        LoginInformation newAdmin = new LoginInformation();
        newAdmin.setUsername("admin");
        newAdmin.setPassword("admin");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(newAdmin);

        given(loginInformationRepository.findByUsername(existingAdmin.getUsername()))
                .willReturn(existingAdmin);

        mvc.perform(post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();
    }


    @Test
    public void adminUserCanLoginWithRegisterUsernameAndReturnToken() throws Exception {
        LoginInformation existingAdmin = new LoginInformation();
        existingAdmin.setUsername("admin");
        existingAdmin.setPassword("admin");


        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(existingAdmin);

        //given register user
        existingAdmin.setPassword(bCryptPasswordEncoder.encode(existingAdmin.getPassword()));
        given(service.loadUserByUsername(existingAdmin.getUsername()))
                .willReturn(new User(existingAdmin.getUsername(), existingAdmin.getPassword(), Collections.emptyList()));

        mvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void adminUserCannotLoginWithWrongPassword() throws Exception{
        //given an register user
        LoginInformation existingAdmin = new LoginInformation();
        existingAdmin.setUsername("admin");
        existingAdmin.setPassword("admin");
        existingAdmin.setPassword(bCryptPasswordEncoder.encode(existingAdmin.getPassword()));
        given(service.loadUserByUsername(existingAdmin.getUsername()))
                .willReturn(new User(existingAdmin.getUsername(), existingAdmin.getPassword(), Collections.emptyList()));

        //given login information with wrong password
        LoginInformation invalidInformation = new LoginInformation();
        invalidInformation.setUsername("admin");
        invalidInformation.setPassword("wrong");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(invalidInformation);

        //When user login, then return unauthorizated
        mvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void whenAdminLoginThenReturnATokenWithUserName() throws Exception {
        //given an register user
        LoginInformation existingAdmin = new LoginInformation();
        existingAdmin.setUsername("admin");
        existingAdmin.setPassword("admin");
        existingAdmin.setPassword(bCryptPasswordEncoder.encode(existingAdmin.getPassword()));
        given(service.loadUserByUsername(existingAdmin.getUsername()))
                .willReturn(new User(existingAdmin.getUsername(), existingAdmin.getPassword(), Collections.emptyList()));

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(existingAdmin);

        //Mocker Login process
        MvcResult mvcResult = mvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized())
                .andReturn();

        //Get JWT Token after Login
        String header = mvcResult.getResponse().getHeader(SecurityConstants.HEADER_NAME);

        //Mock authenication process
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(existingAdmin.getUsername(), existingAdmin.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String jwt = authenticationFilter.jwtGenerator(authentication);

        //assert that the jwt generated is the same with the one that authenication manager generated
        assertThat(jwt, is(header));
    }
}
