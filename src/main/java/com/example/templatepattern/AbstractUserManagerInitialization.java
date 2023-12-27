package com.example.templatepattern;

import com.example.server.ServerUserManager;

public abstract class AbstractUserManagerInitialization {
    /**
     * Повертає ініціалізований ServerUserManager, в якому вже завантажений білий список з користувачами
     * @return ServerUserManager
     */
    public abstract ServerUserManager getInitialisedUserManager();
}
