package com.example.users;

import lombok.*;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.util.ArrayList;
import java.util.List;


@ToString
@Getter
public class UserRole {
    @Setter @Getter
    private static int globalUploadSpeed = Integer.MAX_VALUE;
    @Setter @Getter
    private static int globalDownloadSpeed = Integer.MAX_VALUE;
    private final int id;
    private final boolean isEnabled;
    private final String name;
    private final String homeDir;
    private final boolean isAdmin;
    private final boolean canWrite;
    private final int uploadSpeed;
    private final int downloadSpeed;
    private final List<Authority> authorities = new ArrayList<>();

    public UserRole(int id, boolean isEnabled, String homeDir, boolean isAdmin,
                    int uploadSpeed, int downloadSpeed, boolean canWrite, String name) {
        this.id = id;
        this.isEnabled = isEnabled;
        this.homeDir = homeDir;
        this.isAdmin = isAdmin;
        this.canWrite = canWrite;
        this.uploadSpeed = Math.min(uploadSpeed, globalUploadSpeed);
        this.downloadSpeed = Math.min(downloadSpeed, globalDownloadSpeed);
        this.name = name;
        if (canWrite) {authorities.add(new WritePermission());}
        authorities.add(new CustomTransferRatePermission(downloadSpeed, uploadSpeed));

        authorities.add(new ConcurrentLoginPermission(1, 10));
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public List<Authority> getAuthorities() {
        //Обмеження зі сторони сервера на швидкість
        authorities.stream()
                .filter( e -> e.getClass() == CustomTransferRatePermission.class)
                .map(e -> (CustomTransferRatePermission) e)
                .forEach(e -> {
                    e.setMaxUploadRate(Math.min(uploadSpeed, globalUploadSpeed));
                    e.setMaxDownloadRate(Math.min(downloadSpeed, globalDownloadSpeed));
                });
        return authorities;
    }
}
