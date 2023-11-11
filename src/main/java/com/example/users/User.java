package com.example.users;

import com.example.users.states.AbstractUserState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Setter
@ToString
public class User extends BaseUser{
    @Getter
    private String name = null;
    @Getter
    private String password = null;
    @Getter
    private AbstractUserState state;
    private String homeDir = null;
    private boolean isEnabled = true;
    private List<Authority> authorities = null;

    public User() {
    }


    public String getHomeDirectory() {
        return homeDir != null? homeDir : state.getHomeDir();
    }

    public boolean getEnabled() {
        return state.isEnabled() & isEnabled;
    }


    //верне або параметр юзера, або при відсутності, параметр стейту;
    public List<Authority> getAuthorities() {
        if (authorities.size() == 0) {
            return state.getAuthorities();
        }
        List<Authority> result = new LinkedList<>(authorities);

        boolean hasWritePermission = result.stream()
                .anyMatch(authority -> authority instanceof WritePermission);

        if (!hasWritePermission) {
            state.getAuthorities().stream()
                    .filter(authority -> authority instanceof WritePermission)
                    .findFirst()
                    .ifPresent(result::add);
        }
        boolean hasSpeeds = result.stream()
                .anyMatch(authority -> authority instanceof TransferRatePermission);
        if (!hasSpeeds) {
            state.getAuthorities().stream()
                    .filter(authority -> authority instanceof TransferRatePermission)
                    .findFirst()
                    .ifPresent(result::add);
        }
        return result;
    }
}
