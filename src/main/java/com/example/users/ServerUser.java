package com.example.users;

import lombok.*;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.ftplet.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@ToString

public class ServerUser implements User {
    private String name;
    private String hashedPassword;
    private UserRole role;

    public boolean isAdmin() {
        return role.getIsAdmin();
    }

    public String getRoleName(){
        return role.getName();
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
        return role.getAuthorities();
    }


    @Override
    public int getMaxIdleTime() {
        return 5000;
    }

    @Override
    public boolean getEnabled() {
        return role.getIsEnabled();
    }

    @Override
    public String getHomeDirectory() {
        return role.getHomeDir();
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

    public List<Authority> getAuthorities(Class<? extends Authority> clazz) {
        return role.getAuthorities().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

}



