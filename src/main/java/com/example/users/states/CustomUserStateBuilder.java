package com.example.users.states;

public class CustomUserStateBuilder {
    private String homeDir;
    private String name;
    private boolean isEnabled = true;
    private boolean isAdmin = false;
    private boolean canWrite = false;
    int uploadSpeed = 0;
    int downloadSpeed = 0;

    public CustomUserStateBuilder setHomeDir(String homeDir) {
        this.homeDir = homeDir;
        return this;
    }

    public CustomUserStateBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CustomUserStateBuilder setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    public CustomUserStateBuilder setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }

    public CustomUserStateBuilder setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
        return this;
    }

    public CustomUserStateBuilder setUploadSpeed(int uploadSpeed) {
        this.uploadSpeed = uploadSpeed;
        return this;
    }

    public CustomUserStateBuilder setDownloadSpeed(int downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
        return this;
    }

    public CustomUserState build() {
        return new CustomUserState(isEnabled,homeDir,isAdmin,uploadSpeed,downloadSpeed,canWrite,name);
    }
}

