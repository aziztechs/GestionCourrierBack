package sn.coud.gestioncourrierback.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.coud.gestioncourrierback.dto.UserDTO;
import sn.coud.gestioncourrierback.model.User;
import sn.coud.gestioncourrierback.repository.UserRepository;
import sn.coud.gestioncourrierback.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the UserService interface.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    /**
     * Convert a User entity to a UserDTO.
     * 
     * @param user the User entity to convert
     * @return the UserDTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNom(user.getNom());
        userDTO.setPrenom(user.getPrenom());
        userDTO.setUsername(user.getUsername());
        userDTO.setMatricule(user.getMatricule());
        userDTO.setActive(user.isActive());
        userDTO.setRoleFonction(user.getRoleFonction());
        userDTO.setEmail(user.getEmail());
        // Don't set password in DTO for security reasons
        userDTO.setTelephone(user.getTelephone());
        return userDTO;
    }
    
    /**
     * Convert a UserDTO to a User entity.
     * 
     * @param userDTO the UserDTO to convert
     * @return the User entity
     */
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setNom(userDTO.getNom());
        user.setPrenom(userDTO.getPrenom());
        user.setUsername(userDTO.getUsername());
        user.setMatricule(userDTO.getMatricule());
        user.setActive(userDTO.isActive());
        user.setRoleFonction(userDTO.getRoleFonction());
        user.setEmail(userDTO.getEmail());
        // Only set password if it's not null or empty
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(userDTO.getPassword()); // In a real app, you would hash the password here
        }
        user.setTelephone(userDTO.getTelephone());
        return user;
    }
    
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        // Check if username, email, or matricule already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDTO.getUsername());
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDTO.getEmail());
        }
        if (userRepository.existsByMatricule(userDTO.getMatricule())) {
            throw new IllegalArgumentException("Matricule already exists: " + userDTO.getMatricule());
        }
        
        User user = convertToEntity(userDTO);
        user = userRepository.save(user);
        return convertToDTO(user);
    }
    
    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        
        // Check if username, email, or matricule already exists for another user
        if (!existingUser.getUsername().equals(userDTO.getUsername()) && 
                userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + userDTO.getUsername());
        }
        if (!existingUser.getEmail().equals(userDTO.getEmail()) && 
                userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + userDTO.getEmail());
        }
        if (!existingUser.getMatricule().equals(userDTO.getMatricule()) && 
                userRepository.existsByMatricule(userDTO.getMatricule())) {
            throw new IllegalArgumentException("Matricule already exists: " + userDTO.getMatricule());
        }
        
        // Update user fields
        existingUser.setNom(userDTO.getNom());
        existingUser.setPrenom(userDTO.getPrenom());
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setMatricule(userDTO.getMatricule());
        existingUser.setActive(userDTO.isActive());
        existingUser.setRoleFonction(userDTO.getRoleFonction());
        existingUser.setEmail(userDTO.getEmail());
        // Only update password if it's not null or empty
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(userDTO.getPassword()); // In a real app, you would hash the password here
        }
        existingUser.setTelephone(userDTO.getTelephone());
        
        existingUser = userRepository.save(existingUser);
        return convertToDTO(existingUser);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return convertToDTO(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        return convertToDTO(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        return convertToDTO(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserByMatricule(String matricule) {
        User user = userRepository.findByMatricule(matricule)
                .orElseThrow(() -> new EntityNotFoundException("User not found with matricule: " + matricule));
        return convertToDTO(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    @Override
    public UserDTO setUserActive(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        user.setActive(active);
        user = userRepository.save(user);
        return convertToDTO(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByMatricule(String matricule) {
        return userRepository.existsByMatricule(matricule);
    }
}