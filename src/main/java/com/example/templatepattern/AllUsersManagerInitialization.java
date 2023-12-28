package com.example.templatepattern;

import com.example.server.ServerUserManager;
import com.example.services.UserService;
import com.example.users.ServerUser;
import com.example.users.UserRole;
import lombok.AllArgsConstructor;
import org.apache.ftpserver.usermanager.PasswordEncryptor;

@AllArgsConstructor
public class AllUsersManagerInitialization extends AbstractUserManagerInitialization{
    private UserService userService;
    private PasswordEncryptor passwordEncryptor;

    @Override
    public ServerUserManager getInitialisedUserManager() {
        ServerUserManager manager = new ServerUserManager(passwordEncryptor);
        manager.save(userService.getAllUsers());
        return manager;
    }

}
