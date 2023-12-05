package com.example.users;

import com.example.users.states.AbstractUserState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@ToString
public class ServerUser extends BaseUser implements User {
    private String name;
    private String hashedPassword;
    @Getter
    private AbstractUserState state;

    public boolean isAdmin() {
        return state.isAdmin();
    }

    public String getStateName(){
        return state.getName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return hashedPassword;
    }

    @Override
    public List<Authority> getAuthorities() {
        return state.getAuthorities();
    }

    @Override
    public int getMaxIdleTime() {
        return 5000;
    }

    @Override
    public boolean getEnabled() {
        return state.isEnabled();
    }

    @Override
    public String getHomeDirectory() {
        return state.getHomeDir();
    }

    @Override
    public AuthorizationRequest authorize(AuthorizationRequest authorizationRequest) {
        if (getAuthorities() == null) {
            return null;
        } else {
            boolean someoneCouldAuthorize = false;
            for (Authority authority : getAuthorities()) {
                if (authority.canAuthorize(authorizationRequest)) {
                    someoneCouldAuthorize = true;
                    authorizationRequest = authority.authorize(authorizationRequest);
                    if (authorizationRequest == null) {
                        return null;
                    }
                }
            }
            if (someoneCouldAuthorize) {
                return authorizationRequest;
            } else {
                return null;
            }
        }
    }
  }



