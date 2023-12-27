package com.example.server.states;

import com.example.users.ServerUser;
import com.example.users.UserRole;
import lombok.AllArgsConstructor;
import org.apache.ftpserver.ftplet.FtpletResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class OnlyCertainRolesAccessState extends ServerAccessState {
    private static final Logger logger = LogManager.getRootLogger();
    List<String> roleNames;



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

        //Виключений ліміт по юзерам
        if (maxUsers == 0) {
            logger.info(String.format("%s \"%s\" has connected. (%d/%d)",
                    user.getStateName(), name, activeConnections.get(), maxUsers));

            return FtpletResult.DEFAULT;
        }
        //Ліміт юзерів вже вийшов
        if (activeConnections.get() > maxUsers) {
            //Це адмін
            if (user.isAdmin()) {
                logger.info(String.format("%s \"%s\" has connected. (%d/%d)",
                        user.getStateName(), name, activeConnections.get(), maxUsers));

                return FtpletResult.DEFAULT;
            } else {
                //Це не адмін, кік
                logger.warn(String.format("%s \"%s\" try to connected, but server is full. (%d/%d)",
                        user.getStateName(), name, activeConnections.get() - 1, maxUsers));
                return FtpletResult.DISCONNECT;
            }
        } else {
            //Ліміт ще дозволяє заходити
            logger.info(String.format("%s \"%s\" has logged in successfully. (%d/%d)",
                    user.getStateName(), name, activeConnections.get(), maxUsers));
            return FtpletResult.DEFAULT;
        }
    }
}
