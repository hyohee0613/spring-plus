package org.example.expert.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.user.enums.UserRole;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String nickName;


    @Builder
    public User(Long id, String email, String password, UserRole userRole, String nickName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.nickName = nickName;
    }

    //AuthUser 객체를 User 객체로 변환
    public static User fromAuthUser(AuthUser authUser) {
        return User.builder()
                .id(authUser.getId())
                .email(authUser.getEmail())
                .userRole(authUser.getUserRole())
                .build();
    }

    //비번 변경
    public void changePassword(String password) {
        this.password = password;
    }

    //유저 권한 변경
    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
