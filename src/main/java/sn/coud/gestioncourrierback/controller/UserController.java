package sn.coud.gestioncourrierback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.coud.gestioncourrierback.dto.UserDTO;
import sn.coud.gestioncourrierback.service.UserService;

import java.util.List;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "API for managing users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get a user by id")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/username/{username}")
    @Operation(summary = "Get a user by username")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Get a user by email")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/matricule/{matricule}")
    @Operation(summary = "Get a user by matricule")
    public ResponseEntity<UserDTO> getUserByMatricule(@PathVariable String matricule) {
        UserDTO user = userService.getUserByMatricule(matricule);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping
    @Operation(summary = "Get all users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/active")
    @Operation(summary = "Activate or deactivate a user")
    public ResponseEntity<UserDTO> setUserActive(@PathVariable Long id, @RequestParam boolean active) {
        UserDTO user = userService.setUserActive(id, active);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/check/username/{username}")
    @Operation(summary = "Check if a username exists")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/check/email/{email}")
    @Operation(summary = "Check if an email exists")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/check/matricule/{matricule}")
    @Operation(summary = "Check if a matricule exists")
    public ResponseEntity<Boolean> existsByMatricule(@PathVariable String matricule) {
        boolean exists = userService.existsByMatricule(matricule);
        return ResponseEntity.ok(exists);
    }
}