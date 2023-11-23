package com.example.users;

import com.example.users.states.AbstractUserState;

public class ServerUserBuilderImpl implements ServerUserBuilder {
    private String name;
    private String password;
    private AbstractUserState state;

    @Override
    public ServerUserBuilderImpl setName(String name) {
        this.name = name;
        return this;
    }
    @Override
    public ServerUserBuilderImpl setPassword(String password) {
        this.password = password;
        return this;
    }
    @Override
    public ServerUserBuilderImpl setState(AbstractUserState state) {
        this.state = state;
        return this;
    }


    public ServerUser build() {
        return new ServerUser(this.name, this.password,this.state);
    }
}