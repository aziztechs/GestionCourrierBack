package sn.coud.gestioncourrierback.service;

import sn.coud.gestioncourrierback.dto.UserDTO;

import java.util.List;

/**
 * Service interface for managing users.
 */
public interface UserService {
    
    /**
     * Create a new user.
     * 
     * @param userDTO the user to create
     * @return the created user
     */
    UserDTO createUser(UserDTO userDTO);
    
    /**
     * Update an existing user.
     * 
     * @param id the id of the user to update
     * @param userDTO the updated user data
     * @return the updated user
     */
    UserDTO updateUser(Long id, UserDTO userDTO);
    
    /**
     * Get a user by id.
     * 
     * @param id the id of the user to get
     * @return the user
     */
    UserDTO getUserById(Long id);
    
    /**
     * Get a user by username.
     * 
     * @param username the username of the user to get
     * @return the user
     */
    UserDTO getUserByUsername(String username);
    
    /**
     * Get a user by email.
     * 
     * @param email the email of the user to get
     * @return the user
     */
    UserDTO getUserByEmail(String email);
    
    /**
     * Get a user by matricule.
     * 
     * @param matricule the matricule of the user to get
     * @return the user
     */
    UserDTO getUserByMatricule(String matricule);
    
    /**
     * Get all users.
     * 
     * @return a list of all users
     */
    List<UserDTO> getAllUsers();
    
    /**
     * Delete a user by id.
     * 
     * @param id the id of the user to delete
     */
    void deleteUser(Long id);
    
    /**
     * Activate or deactivate a user.
     * 
     * @param id the id of the user to activate or deactivate
     * @param active true to activate, false to deactivate
     * @return the updated user
     */
    UserDTO setUserActive(Long id, boolean active);
    
    /**
     * Check if a username exists.
     * 
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if an email exists.
     * 
     * @param email the email to check
     * @return true if the email exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if a matricule exists.
     * 
     * @param matricule the matricule to check
     * @return true if the matricule exists, false otherwise
     */
    boolean existsByMatricule(String matricule);
}