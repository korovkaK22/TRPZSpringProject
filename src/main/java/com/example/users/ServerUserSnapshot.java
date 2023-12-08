package com.example.users;

import com.example.entity.UserSnapshotEntity;
import com.example.users.states.AbstractUserState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ServerUserSnapshot {
    private final String name;
    private final String hashedPassword;
    private final AbstractUserState state;
}

