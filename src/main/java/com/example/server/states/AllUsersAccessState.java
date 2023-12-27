package com.example.server.states;

import com.example.users.ServerUser;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class AllUsersAccessState extends ServerAccessState {
    private static final Logger logger = LogManager.getRootLogger();


    /**
     * Дозволяє зайти по ліміту користувачам, і адмінам безлімітно
     * @param user користувач, який заходить
     * @param maxUsers максимальна кількість людей
     * @param activeConnections кількість людей на сервері
     * @return результат
     */
    @Override
    public FtpletResult checkServerAccess(ServerUser user, int maxUsers, AtomicInteger activeConnections) {
        String name = user.getName();
        String role = user.getRoleName();

        //Виключений ліміт по юзерам
        if (maxUsers == 0) {
            logger.info(String.format("%s \"%s\" has connected. (%d/%d)",
                    role, name, activeConnections.get(), maxUsers));

            return FtpletResult.DEFAULT;
        }
        //Ліміт юзерів вже вийшов
        if (activeConnections.get() > maxUsers) {
            //Це адмін
            if (user.isAdmin()) {
                logger.info(String.format("%s \"%s\" has connected. (%d/%d)",
                        role, name, activeConnections.get(), maxUsers));

                return FtpletResult.DEFAULT;
            } else {
                //Це не адмін, кік
                logger.warn(String.format("%s \"%s\" try to connected, but server is full. (%d/%d)",
                        role, name, activeConnections.get() - 1, maxUsers));
                return FtpletResult.DISCONNECT;
            }
        } else {
            //Ліміт ще дозволяє заходити
            logger.info(String.format("%s \"%s\" has logged in successfully. (%d/%d)",
                    role, name, activeConnections.get(), maxUsers));
            return FtpletResult.DEFAULT;
        }
    }
}
