package com.example.users.states;


public class CustomUserState extends AbstractUserState{

    public CustomUserState(boolean isEnabled, String homeDir, boolean isAdmin,
                           int uploadSpeed, int downloadSpeed, boolean canWrite, String name) {
        super(isEnabled, homeDir, isAdmin, uploadSpeed, downloadSpeed, canWrite, name);
    }
}
