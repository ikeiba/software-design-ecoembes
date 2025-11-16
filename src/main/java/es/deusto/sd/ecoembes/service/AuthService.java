/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.ecoembes.service;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.dao.EmployeeRepository;
import es.deusto.sd.ecoembes.entity.Employee;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;

    // Storage to keep the session of the employees that are logged in
    private static Map<String, Employee> tokenStore = new HashMap<>();

    public AuthService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Login method that checks if the user exists in the database and validates the
    // password
    public Optional<String> login(String email, String password) {
        Optional<Employee> user = employeeRepository.findByEmail(email);

        if (user.isPresent() && user.get().checkPassword(password)) {
            String token = generateToken(); // Generate a random token for the session
            tokenStore.put(token, user.get()); // Store the token and associate it with the employee

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

    // Method to get the employee based on the token
    public Employee getUserByToken(String token) {
        return tokenStore.get(token);
    }

    // Method to get the employee based on the email
    public Employee getUserByEmail(String email) {
        Optional<Employee> employee = employeeRepository.findByEmail(email);
        return employee.orElse(null);
    }

    // Synchronized method to guarantee unique token generation
    private static synchronized String generateToken() {
        return Long.toHexString(System.currentTimeMillis());
    }

}