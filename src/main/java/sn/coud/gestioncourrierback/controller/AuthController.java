package sn.coud.gestioncourrierback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sn.coud.gestioncourrierback.dto.*;
import sn.coud.gestioncourrierback.security.CustomUserDetailsService;
import sn.coud.gestioncourrierback.security.JwtTokenProvider;
import sn.coud.gestioncourrierback.service.UserService;

/**
 * REST controller for authentication operations.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API for user authentication")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return JWT token")
    public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        
        // Get user details
        UserDTO user = userService.getUserByUsername(loginRequest.getUsername());
        
        // Calculate expiration time (24 hours from now)
        long expiresIn = 86400000L; // 24 hours in milliseconds
        
        JwtAuthenticationResponse response = new JwtAuthenticationResponse(jwt, expiresIn, user);
        
        return ResponseEntity.ok(ApiResponse.success("Connexion réussie", response));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@Valid @RequestBody UserDTO userDTO) {
        
        // Check if username already exists
        if (userService.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Erreur : Le nom d'utilisateur est déjà utilisé !"));
        }

        // Check if email already exists
        if (userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Erreur : L'email est déjà utilisé !"));
        }

        // Check if matricule already exists
        if (userService.existsByMatricule(userDTO.getMatricule())) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Erreur : Le matricule est déjà utilisé !"));
        }

        // Create new user
        UserDTO result = userService.createUser(userDTO);
        
        return ResponseEntity.ok(ApiResponse.success("Utilisateur créé avec succès", result));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user information")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser(Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Utilisateur non authentifié"));
        }

        CustomUserDetailsService.UserPrincipal userPrincipal = 
            (CustomUserDetailsService.UserPrincipal) authentication.getPrincipal();
        
        UserDTO user = userService.getUserById(userPrincipal.getId());
        
        return ResponseEntity.ok(ApiResponse.success("Informations utilisateur récupérées", user));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh JWT token")
    public ResponseEntity<ApiResponse<JwtAuthenticationResponse>> refreshToken(Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Utilisateur non authentifié"));
        }

        String jwt = tokenProvider.generateToken(authentication);
        
        CustomUserDetailsService.UserPrincipal userPrincipal = 
            (CustomUserDetailsService.UserPrincipal) authentication.getPrincipal();
        
        UserDTO user = userService.getUserById(userPrincipal.getId());
        
        long expiresIn = 86400000L; // 24 hours in milliseconds
        
        JwtAuthenticationResponse response = new JwtAuthenticationResponse(jwt, expiresIn, user);
        
        return ResponseEntity.ok(ApiResponse.success("Token rafraîchi avec succès", response));
    }
}

