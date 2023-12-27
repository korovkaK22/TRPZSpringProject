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
    private final boolean isEnabled;
    private final String name;
    private final String homeDir;
    private final boolean isAdmin;
    private final boolean canWrite;
    private final int uploadSpeed;
    private final int downloadSpeed;
    private final List<Authority> authorities = new ArrayList<>();

    public UserRole( boolean isEnabled, String homeDir, boolean isAdmin,
                    int uploadSpeed, int downloadSpeed, boolean canWrite, String name) {
        this.isEnabled = isEnabled;
        this.homeDir = homeDir;
        this.isAdmin = isAdmin;
        this.canWrite = canWrite;
        this.uploadSpeed = uploadSpeed;
        this.downloadSpeed = downloadSpeed;
        this.name = name;
        if (canWrite) {authorities.add(new WritePermission());}
        authorities.add(new TransferRatePermission(downloadSpeed, uploadSpeed));

        authorities.add(new ConcurrentLoginPermission(1, 10));
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }
}
