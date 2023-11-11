package com.example.users.states;

public class PremiumUserState extends AbstractUserState{
    public PremiumUserState(boolean isEnabled, String homeDir, boolean isAdmin, int uploadSpeed, int downloadSpeed, boolean canWrite) {
        super(isEnabled, homeDir, isAdmin, uploadSpeed, downloadSpeed, canWrite);
    }
}
