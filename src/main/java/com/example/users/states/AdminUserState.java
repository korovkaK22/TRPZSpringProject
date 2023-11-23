package com.example.users.states;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


public class AdminUserState extends AbstractUserState{

    public AdminUserState(boolean isEnabled, String homeDir, boolean isAdmin,
                          int uploadSpeed, int downloadSpeed, boolean canWrite, String name) {
        super(isEnabled, homeDir, isAdmin, uploadSpeed, downloadSpeed, canWrite, name);
    }
}
