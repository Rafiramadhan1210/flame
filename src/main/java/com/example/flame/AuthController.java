package com.example.flame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // POST /api/auth/register
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");

        Map<String, Object> response = new HashMap<>();

        if (userRepository.existsByUsername(username)) {
            response.put("success", false);
            response.put("message", "Username sudah dipakai");
            return response;
        }
        if (userRepository.existsByEmail(email)) {
            response.put("success", false);
            response.put("message", "Email sudah dipakai");
            return response;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        userRepository.save(user);

        response.put("success", true);
        response.put("message", "Registrasi berhasil");
        response.put("username", username);
        return response;
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Map<String, Object> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User u = userOpt.get();
            response.put("success", true);
            response.put("message", "Login berhasil");
            response.put("username", u.getUsername());
            response.put("id", u.getId());
        } else {
            response.put("success", false);
            response.put("message", "Username atau password salah");
        }

        return response;
    }

    @GetMapping("/user/{id}")
    public Map<String, Object> getUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User u = userOpt.get();
            response.put("success", true);
            response.put("id", u.getId());
            response.put("username", u.getUsername());
            response.put("email", u.getEmail());
        } else {
            response.put("success", false);
            response.put("message", "User tidak ditemukan");
        }
        return response;
    }

    @PutMapping("/user/{id}")
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "User tidak ditemukan");
            return response;
        }
        User user = userOpt.get();
        String newUsername = body.get("username");
        String newEmail = body.get("email");
        String newPassword = body.get("password");

        if (newUsername != null && !newUsername.equals(user.getUsername()) && userRepository.existsByUsername(newUsername)) {
            response.put("success", false);
            response.put("message", "Username sudah dipakai");
            return response;
        }
        if (newEmail != null && !newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
            response.put("success", false);
            response.put("message", "Email sudah dipakai");
            return response;
        }

        if (newUsername != null) user.setUsername(newUsername);
        if (newEmail != null) user.setEmail(newEmail);
        if (newPassword != null) user.setPassword(newPassword);
        userRepository.save(user);

        response.put("success", true);
        response.put("message", "Profil berhasil diperbarui");
        return response;
    }

    @DeleteMapping("/user/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        if (!userRepository.existsById(id)) {
            response.put("success", false);
            response.put("message", "User tidak ditemukan");
            return response;
        }
        userRepository.deleteById(id);
        response.put("success", true);
        response.put("message", "User berhasil dihapus");
        return response;
    }
}