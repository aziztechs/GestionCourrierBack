package sn.coud.gestioncourrierback.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.coud.gestioncourrierback.model.User;
import sn.coud.gestioncourrierback.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom UserDetailsService implementation for Spring Security.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur : " + username));

        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'ID : " + id));

        return UserPrincipal.create(user);
    }

    /**
     * Custom UserDetails implementation.
     */
    public static class UserPrincipal implements UserDetails {
        private Long id;
        private String nom;
        private String prenom;
        private String username;
        private String email;
        private String password;
        private boolean active;
        private Collection<? extends GrantedAuthority> authorities;

        public UserPrincipal(Long id, String nom, String prenom, String username, String email, 
                            String password, boolean active, Collection<? extends GrantedAuthority> authorities) {
            this.id = id;
            this.nom = nom;
            this.prenom = prenom;
            this.username = username;
            this.email = email;
            this.password = password;
            this.active = active;
            this.authorities = authorities;
        }

        public static UserPrincipal create(User user) {
            Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRoleFonction().toUpperCase())
            );

            return new UserPrincipal(
                user.getId(),
                user.getNom(),
                user.getPrenom(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isActive(),
                authorities
            );
        }

        // Getters
        public Long getId() { return id; }
        public String getNom() { return nom; }
        public String getPrenom() { return prenom; }
        public String getEmail() { return email; }

        @Override
        public String getUsername() { return username; }

        @Override
        public String getPassword() { return password; }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

        @Override
        public boolean isAccountNonExpired() { return true; }

        @Override
        public boolean isAccountNonLocked() { return true; }

        @Override
        public boolean isCredentialsNonExpired() { return true; }

        @Override
        public boolean isEnabled() { return active; }
    }
}

