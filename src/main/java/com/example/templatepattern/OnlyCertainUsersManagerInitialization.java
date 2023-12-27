package com.example.templatepattern;

import com.example.server.ServerUserManager;
import com.example.services.UserService;
import lombok.AllArgsConstructor;
import org.apache.ftpserver.usermanager.PasswordEncryptor;

@AllArgsConstructor
public class OnlyCertainUsersManagerInitialization extends AbstractUserManagerInitialization{
    private UserService userService;
    private PasswordEncryptor passwordEncryptor;

    @Override
    public ServerUserManager getInitialisedUserManager() {
        ServerUserManager manager = new ServerUserManager(passwordEncryptor);
        manager.save(userService.getOnlyAdminUsers());
        return manager;
    }

}
