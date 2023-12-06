package com.example.users;

import com.example.users.states.AbstractUserState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServerUserSnapshot {
    private final String name;
    private final String hashedPassword;
    private final AbstractUserState state;

}
