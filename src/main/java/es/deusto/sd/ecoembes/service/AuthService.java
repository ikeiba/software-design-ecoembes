/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.ecoembes.service;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.dto.LoginDTO;
import es.deusto.sd.ecoembes.dto.TokenDTO;
import es.deusto.sd.ecoembes.entity.Employee;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

        // Simulating an employee repository
        private static Map<String, Employee> userRepository = new HashMap<>();

        // Storage to keep the session of the employees that are logged in
        private static Map<String, Employee> tokenStore = new HashMap<>();

    // Login method that checks if the user exists in the database and validates the
    // password
    public Optional<String> login(String email, String password) {
        Employee user = userRepository.get(email);

        if (user != null && user.checkPassword(password)) {
            String token = generateToken(); // Generate a random token for the session
            tokenStore.put(token, user); // Store the token and associate it with the employee

            return Optional.of(token);
        } else {
            return Optional.empty();
        }
    }

    // Logout method to remove the token from the session store
    public Optional<Boolean> logout(String token) {
        if (tokenStore.containsKey(token)) {
            tokenStore.remove(token);

            return Optional.of(true);
        } else {
            return Optional.empty();
        }
    }

    // Method to add a new employee to the repository
    public void addEmployee(Employee user) {
        if (user != null) {
            userRepository.putIfAbsent(user.getEmail(), user);
        }
    }

    // New API: login using DTO and returning TokenDTO
    public TokenDTO loginUser(LoginDTO credentials) {
        if (credentials == null) throw new IllegalArgumentException("Missing credentials");

        Optional<String> token = login(credentials.getEmail(), credentials.getPassword());
        if (token.isPresent()) {
            return new TokenDTO(token.get());
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    // New API: logout by token (void)
    public void logoutUser(String token) {
        if (token == null) throw new IllegalArgumentException("Missing token");

        Optional<Boolean> result = logout(token);
        if (!result.isPresent() || !result.get()) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    // Method to get the employee based on the token
    public Employee getUserByToken(String token) {
        return tokenStore.get(token);
    }

    // Method to get the employee based on the email
    public Employee getUserByEmail(String email) {
        return userRepository.get(email);
    }

    // Synchronized method to guarantee unique token generation
    private static synchronized String generateToken() {
        return Long.toHexString(System.currentTimeMillis());
    }
}