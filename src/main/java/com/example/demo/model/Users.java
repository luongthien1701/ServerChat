package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String phone;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private String status;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "last_login")
    private Date lastLogin;

    private String roles;
    public enum Role {
        USER, ADMIN
    }

    @Column(name = "is_verified")
    private boolean isVerified;

    @Column(name = "deleted_at")
    private Boolean deletedAt;
}
