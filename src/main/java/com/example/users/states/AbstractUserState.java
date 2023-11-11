package com.example.users.states;

import lombok.*;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.util.ArrayList;
import java.util.List;


@ToString
@Getter
public class AbstractUserState {
    private final boolean isEnabled;
    private final String homeDir;
    private final boolean isAdmin;
    private final boolean canWrite;
    private final List<Authority> authorities = new ArrayList<>();

    public AbstractUserState(boolean isEnabled, String homeDir, boolean isAdmin, int uploadSpeed, int downloadSpeed, boolean canWrite) {
        this.isEnabled = isEnabled;
        this.homeDir = homeDir;
        this.isAdmin = isAdmin;
        this.canWrite = canWrite;
        if (canWrite) authorities.add(new WritePermission());
        authorities.add(new TransferRatePermission(downloadSpeed, uploadSpeed));
    }


}
