package Group12.Stockify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.stockify.useraccountservice.dto.RegistrationRequest;
import com.stockify.useraccountservice.dto.LoginRequest;
import com.stockify.useraccountservice.Controller.MainController;
import com.stockify.useraccountservice.Repository.UserRepository;
import com.stockify.useraccountservice.dto.*;
import com.stockify.useraccountservice.Model.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

class StockifyApplicationTests {

//	@Test
//	void contextLoads() {
//	}

    @InjectMocks
    private MainController mainController;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(userRepository.save(any(User.class))).thenReturn(new User()); // Example behavior for save method
    }

    @Test
    public void testValidRegistration() {
        RegistrationRequest validRequest = new RegistrationRequest();
        validRequest.setFirstName("firstname");
        validRequest.setLastName("lastname");
        validRequest.setEmail("email@email.com");
        validRequest.setPassword("password");
        validRequest.setConfirmPassword("password");
        validRequest.setBusiness("business");

        when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = mainController.register(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully.", response.getBody().getMessage());
    }

    @Test
    public void testDifferentConfirmPasswordRegistration() {
        RegistrationRequest request = new RegistrationRequest();
        request.setFirstName("firstname");
        request.setLastName("lastname");
        request.setEmail("email@email.com");
        request.setPassword("password");
        request.setConfirmPassword("differentPassword");
        request.setBusiness("business");

        when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = mainController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Passwords do not match.", response.getBody().getMessage());
    }

    @Test
    public void testExistingUserRegistration() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFirstName("firstname");
        registrationRequest.setLastName("lastname");
        registrationRequest.setEmail("email@email.com");
        registrationRequest.setPassword("password");
        registrationRequest.setConfirmPassword("password");
        registrationRequest.setBusiness("business");

        User user = new User();
        user.setEmail("email@email.com");

        Optional<User> optionalUser = Optional.ofNullable(user);

        when(userRepository.findByEmail("email@email.com")).thenReturn(optionalUser);

        ResponseEntity<ApiResponse> response = mainController.register(registrationRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email already exists.", response.getBody().getMessage());
    }

    @Test
    public void testEmptyFieldsRegistration() {
        RegistrationRequest validRequest = new RegistrationRequest();

        when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> {
            mainController.register(validRequest);
        });
    }

    @Test
    public void testValidLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("email@email.com");
        loginRequest.setPassword("password");

        User user = new User();
        user.setEmail("email@email.com");
        user.setPassword("password");

        Optional<User> optionalUser = Optional.ofNullable(user);

        when(userRepository.findByEmail("email@email.com")).thenReturn(optionalUser);

        ResponseEntity<ApiResponse> response = mainController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User logged in successfully!", response.getBody().getMessage());
    }

    @Test
    public void testWrongPasswordLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("email@email.com");
        loginRequest.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("email@email.com");
        user.setPassword("password");

        Optional<User> optionalUser = Optional.ofNullable(user);

        when(userRepository.findByEmail("email@email.com")).thenReturn(optionalUser);

        ResponseEntity<ApiResponse> response = mainController.login(loginRequest);

        assertEquals("Incorrect password!", response.getBody().getMessage());
    }

    @Test
    public void testNonexistentUserLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("email@email.com");
        loginRequest.setPassword("wrongPassword");

        when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = mainController.login(loginRequest);

        assertEquals("User does not exist!", response.getBody().getMessage());
    }

    @Test
    public void testValidGetUserId() {
        // Register a user to generate a user ID
        RegistrationRequest validRequest = new RegistrationRequest();
        validRequest.setFirstName("firstname");
        validRequest.setLastName("lastname");
        validRequest.setEmail("email@email.com");
        validRequest.setPassword("password");
        validRequest.setConfirmPassword("password");
        validRequest.setBusiness("business");

        when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = mainController.register(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully.", response.getBody().getMessage());

        // GetUserId test
        NameEmailRequest nameEmailRequest = new NameEmailRequest();
        nameEmailRequest.setFirstName("firstName");
        nameEmailRequest.setLastName("lastName");
        nameEmailRequest.setEmail("email@email.com");

        response = mainController.getUserIdByFirstNameAndLastName(nameEmailRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testInvalidUserGetUserId() {
        NameEmailRequest nameEmailRequest = new NameEmailRequest();
        nameEmailRequest.setFirstName("firstName");
        nameEmailRequest.setLastName("lastName");
        nameEmailRequest.setEmail("email@email.com");

        when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = mainController.getUserIdByFirstNameAndLastName(nameEmailRequest);

        assertEquals("-1", response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatusCode());
    }

    @Test
    public void testValidCheckUserExists() {
        // Register a user to generate a user ID
        RegistrationRequest validRequest = new RegistrationRequest();
        validRequest.setFirstName("firstname");
        validRequest.setLastName("lastname");
        validRequest.setEmail("email@email.com");
        validRequest.setPassword("password");
        validRequest.setConfirmPassword("password");
        validRequest.setBusiness("business");

        when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = mainController.register(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully.", response.getBody().getMessage());

        // GetUserId test
        NameEmailRequest nameEmailRequest = new NameEmailRequest();
        nameEmailRequest.setFirstName("firstName");
        nameEmailRequest.setLastName("lastName");
        nameEmailRequest.setEmail("email@email.com");

        response = mainController.getUserIdByFirstNameAndLastName(nameEmailRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testInvalidCheckUserExist() {
        NameEmailRequest nameEmailRequest = new NameEmailRequest();
        nameEmailRequest.setFirstName("firstName");
        nameEmailRequest.setLastName("lastName");
        nameEmailRequest.setEmail("email@email.com");

        when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = mainController.getUserIdByFirstNameAndLastName(nameEmailRequest);

        assertEquals(404, response.getBody().getStatusCode());
    }

    @Test
    public void testIsBusinessCheckIfBusiness() {
        User user = new User();
        user.setBusiness("business");

        Optional<User> optionalUser = Optional.ofNullable(user);

        when(userRepository.findByEmail("email@email.com")).thenReturn(optionalUser);

        ResponseEntity<ApiResponse> response = mainController.checkIfBusiness("email@email.com");

        assertEquals(200, response.getBody().getStatusCode());
        assertEquals("User is a business.", response.getBody().getMessage());
    }

    @Test
    public void testIsNotBusinessCheckIfBusiness() {
        User user = new User();

        Optional<User> optionalUser = Optional.ofNullable(user);

        when(userRepository.findByEmail("email@email.com")).thenReturn(optionalUser);

        ResponseEntity<ApiResponse> response = mainController.checkIfBusiness("email@email.com");

        assertEquals(204, response.getBody().getStatusCode());
        assertEquals("User is not a business.", response.getBody().getMessage());
    }

    @Test
    public void testNoUserCheckIfBusiness() {
        when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse> response = mainController.checkIfBusiness("email@email.com");

        assertEquals(404, response.getBody().getStatusCode());
        assertEquals("User does not exist!", response.getBody().getMessage());
    }
}
