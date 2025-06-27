package org.example.expert.domain.user.entity;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private final Long userId;
    private final String email;
    private final String role; // 예: ROLE_USER

    public CustomUserDetails(User user) {
        super(user.getEmail(), user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name())));
        this.userId = user.getId();
        this.email = user.getEmail();
        this.role = "ROLE_" + user.getUserRole().name();
    }

    public CustomUserDetails(Long userId, String email, String role) {
        super(email, "", List.of(new SimpleGrantedAuthority(role)));
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    // 직접 추가
    public String getRole() {
        return this.role;
    }
}
