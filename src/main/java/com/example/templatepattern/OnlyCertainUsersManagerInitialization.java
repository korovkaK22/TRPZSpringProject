package com.example.templatepattern;

import com.example.server.ServerUserManager;
import com.example.services.UserService;
import org.apache.ftpserver.usermanager.PasswordEncryptor;


public class OnlyCertainUsersManagerInitialization extends AbstractUserManagerInitialization{
    private final UserService userService;
    private final PasswordEncryptor passwordEncryptor;
    private final String[] usernames;

    public OnlyCertainUsersManagerInitialization(UserService userService, PasswordEncryptor passwordEncryptor, String... usernames) {
        this.userService = userService;
        this.passwordEncryptor = passwordEncryptor;
        this.usernames = usernames;
    }

    @Override
    public ServerUserManager getInitialisedUserManager() {
        ServerUserManager manager = new ServerUserManager(passwordEncryptor);
        manager.save(userService.getUsersByName(usernames));
        return manager;
    }

}
