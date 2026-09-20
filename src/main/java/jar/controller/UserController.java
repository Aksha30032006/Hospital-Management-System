
package jar.controller;

import jar.entity.User;
import jar.repository.UserRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // Get all users
    @GetMapping
    public List<User> getAllUsers() {

        return userRepository.findAll();

    }


    // Register new user
    @PostMapping("/register")
    public String register(@RequestBody User user) {

        // Check username already exists

        List<User> users = userRepository.findAll();

        for (User existingUser : users) {

            if (existingUser.getUsername()
                    .equals(user.getUsername())) {

                return "Username already exists";

            }

        }


        // Save new user

        userRepository.save(user);

        return "Registration Successful";

    }


    // Login
    @PostMapping("/login")
    public String login(@RequestBody User user) {

        List<User> users = userRepository.findAll();

        for (User existingUser : users) {

            if (existingUser.getUsername()
                    .equals(user.getUsername())
                    &&
                existingUser.getPassword()
                    .equals(user.getPassword())) {

                return "Login Successful";

            }

        }

        return "Invalid Username or Password";

    }

}

