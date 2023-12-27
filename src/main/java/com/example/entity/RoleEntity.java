package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(nullable = false, length = 255)
    private String name;

    @NotNull
    @Column(nullable = false, length = 255)
    private String path;

    @NotNull
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    @NotNull
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;

    @NotNull
    @Column(name = "can_write", nullable = false)
    private Boolean canWrite;

    @NotNull
    @Column(name = "upload_speed", nullable = false)
    private Integer uploadSpeed;

    @NotNull
    @Column(name = "download_speed", nullable = false)
    private Integer downloadSpeed;

}
