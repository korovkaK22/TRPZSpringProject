package com.example.server.states;

import com.example.users.ServerUser;
import lombok.AllArgsConstructor;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class NoNewUsersState extends ServerAccessState {
    private static final Logger logger = LogManager.getRootLogger();


    /**
     * Нікому не дозволяє зайти на сервер
     *
     * @param user              користувач, який заходить
     * @param maxUsers          максимальна кількість людей
     * @param activeConnections кількість людей на сервері
     * @return результат
     */
    @Override
    public FtpletResult checkServerAccess(ServerUser user, int maxUsers, AtomicInteger activeConnections) {
        String name = user.getName();


        logger.warn(String.format("%s \"%s\" try to connected, but server is in noNewUser mode. (%d/%d)",
                user.getRoleName(), name, activeConnections.get() - 1, maxUsers));
        return FtpletResult.DISCONNECT;


    }
}
