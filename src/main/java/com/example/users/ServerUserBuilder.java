package com.example.users;

import com.example.users.states.AbstractUserState;

public class ServerUserBuilder {
    private String name;
    private String hashedPassword;
    private AbstractUserState state;

    public ServerUserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ServerUserBuilder setPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
        return this;
    }

    public ServerUserBuilder setState(AbstractUserState state) {
        this.state = state;
        return this;
    }

    public ServerUser build() {
        return new ServerUser(name, hashedPassword, state);
    }
}
