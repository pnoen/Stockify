package Group12.Stockify;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.SecureRandom;
import java.sql.Array;
import java.util.*;

@RestController
public class UserAccountController {

    private SessionFactory factory = new Configuration()
                                .configure("hibernate.cfg.xml")
                                .addAnnotatedClass(User.class)
                                .buildSessionFactory();

    private UserDAO userDAO = new UserDAOImpl(factory);
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String business
    ) {

        // Check email
        if (userDAO.getUser(email) == null) {
            return ResponseEntity.status(401).body("Email already exists!");
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            return ResponseEntity.status(401).body("Passwords do not match");
        }

        // Create new user object
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setBusiness(business);

        try {
            userDAO.saveUser(newUser);
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error!");
        }

//        // Save the newUser object to the database (replace this line with actual DB operations)
//        // Hash and salt the pw before saving it to db
//        users.add(newUser);
//
//        return ResponseEntity.ok("User registered successfully!");
    }

    // Login an existing user
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam User user) {
        // Check if the user exists in the database and the password matches (replace this with actual DB operations)
//        for (User existingUser : users) {
//            if (existingUser.getEmail().equals(user.getEmail()) && existingUser.getPassword().equals(user.getPassword())) {
//                // Successful login logic here (like generating JWT token, etc.)
//                return ResponseEntity.ok("Logged in successfully!");
//            }
//        }

        return ResponseEntity.status(401).body("Invalid email or password!");
    }
}