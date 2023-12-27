package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 255)
    private String username;

    @NotNull
    @Column(nullable = false, length = 255)
    private String password;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

}

