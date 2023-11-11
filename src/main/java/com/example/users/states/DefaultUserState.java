package com.example.users.states;

import org.springframework.stereotype.Component;


public class DefaultUserState extends AbstractUserState{
    public DefaultUserState(boolean isEnabled, String homeDir, boolean isAdmin, int uploadSpeed, int downloadSpeed, boolean canWrite) {
        super(isEnabled, homeDir, isAdmin, uploadSpeed, downloadSpeed, canWrite);
    }
}
