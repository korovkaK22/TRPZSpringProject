package com.example.users;

import com.example.users.states.AbstractUserState;

public interface ServerUserBuilder {

    ServerUserBuilder setName(String name);

    ServerUserBuilder setPassword(String hashedPassword);

    ServerUserBuilder setState(AbstractUserState state);

    ServerUser build();
}
