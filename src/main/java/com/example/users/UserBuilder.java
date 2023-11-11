package com.example.users;

import com.example.users.states.AbstractUserState;
import org.apache.ftpserver.ftplet.Authority;

import java.util.List;

public class UserBuilder {
    private String name;
    private String password;
    private AbstractUserState state;
    private String homeDir;
    private boolean isEnabled = true;
    private List<Authority> authorities;

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setState(AbstractUserState state) {
        this.state = state;
        return this;
    }

    public UserBuilder setHomeDir(String homeDir) {
        this.homeDir = homeDir;
        return this;
    }

    public UserBuilder setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    public UserBuilder setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public User build() {
        User user = new User();
        user.setName(this.name);
        user.setPassword(this.password);
        user.setState(this.state);
        user.setHomeDir(this.homeDir);
        user.setEnabled(this.isEnabled);
        user.setAuthorities(this.authorities);
        return user;
    }
}

