package com.example.users.states;

public class BannedUserState extends AbstractUserState{
    public BannedUserState(boolean isEnabled, String homeDir, boolean isAdmin, int uploadSpeed, int downloadSpeed, boolean canWrite) {
        super(isEnabled, homeDir, isAdmin, uploadSpeed, downloadSpeed, canWrite);
    }
}
