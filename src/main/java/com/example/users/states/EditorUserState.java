package com.example.users.states;

public class EditorUserState extends AbstractUserState{
    public EditorUserState(boolean isEnabled, String homeDir, boolean isAdmin, int uploadSpeed, int downloadSpeed, boolean canWrite) {
        super(isEnabled, homeDir, isAdmin, uploadSpeed, downloadSpeed, canWrite);
    }
}
