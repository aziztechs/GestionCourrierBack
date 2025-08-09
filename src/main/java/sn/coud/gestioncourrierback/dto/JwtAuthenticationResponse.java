package sn.coud.gestioncourrierback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for JWT authentication response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {
    
    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserDTO user;
    
    public JwtAuthenticationResponse(String accessToken, Long expiresIn, UserDTO user) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.user = user;
    }
}

