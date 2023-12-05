package com.example.entity;

import com.example.users.states.CustomUserState;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "custom_states")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomStateEntity {

    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @Column(name = "direction", nullable = false)
    private String direction;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled;

    @Column(name = "can_write", nullable = false)
    private boolean canWrite;

    @Column(name = "upload_speed", nullable = false)
    private int uploadSpeed;

    @Column(name = "download_speed", nullable = false)
    private int downloadSpeed;


    public CustomStateEntity(CustomUserState state) {
        this.name = state.getName();
        this.isAdmin = state.isAdmin();
        this.direction = state.getHomeDir();
        this.isEnabled = state.isEnabled();
        this.canWrite = state.isCanWrite();
        this.uploadSpeed = state.getUploadSpeed();
        this.downloadSpeed = state.getDownloadSpeed();
    }
}
