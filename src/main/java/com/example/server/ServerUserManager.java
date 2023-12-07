package com.example.server;

import com.example.security.PasswordEncryptorImpl;
import com.example.users.ServerUser;
import com.example.users.states.CustomUserState;
import com.example.users.states.CustomUserStateBuilder;
import lombok.AllArgsConstructor;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.springframework.stereotype.Component;

import java.util.*;


@AllArgsConstructor
public class ServerUserManager implements UserManager {
    private final Map<String, User> users = new HashMap<>();
    private PasswordEncryptor passwordEncryptor;

    public void save(List<ServerUser> users) {
        for (User user : users){
            save(user);
        }
    }

    public Optional<ServerUser> getServerUserByName(String userName) {
        if (!this.doesExist(userName)) {
            return Optional.empty();
        } else {
            User user = users.get(userName);
            return user instanceof ServerUser ? Optional.of((ServerUser) user) : Optional.empty();
        }
    }

    @Override
    public User getUserByName(String userName) {
        if (!this.doesExist(userName)) {
            return null;
        } else {
            return users.get(userName);
        }
    }


    @Override
    public String[] getAllUserNames() {
        return users.keySet().toArray(new String[0]);
    }

    @Override
    public void delete(String username) {
        users.remove(username);
    }

    @Override
    public void save(User user) {
        users.put(user.getName(), user);
    }


    @Override
    public boolean doesExist(String username) {
        return users.containsKey(username);
    }

    @Override
    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
        if (authentication instanceof UsernamePasswordAuthentication upAuth) {
            String username = upAuth.getUsername();
            String password = upAuth.getPassword();


            User user = users.get(username);
            if (user != null && passwordEncryptor.matches(password, user.getPassword())) {
                return user;
            }
        }
        throw new AuthenticationFailedException();
    }

    @Override
    public String getAdminName() {
        return "admin";
    }

    @Override
    public boolean isAdmin(String username) {
        if ("admin".equals(username)){
            return true;
        }
        User user = getUserByName(username);
        if (user instanceof ServerUser serverUser){
            return serverUser.getState().getIsAdmin();
        }
       return false;
    }
}
