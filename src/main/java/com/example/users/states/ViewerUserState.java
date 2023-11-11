package com.example.users.states;


public class ViewerUserState extends AbstractUserState{
    public ViewerUserState(boolean isEnabled, String homeDir, boolean isAdmin, int uploadSpeed, int downloadSpeed, boolean canWrite) {
        super(isEnabled, homeDir, isAdmin, uploadSpeed, downloadSpeed, canWrite);
    }
}
