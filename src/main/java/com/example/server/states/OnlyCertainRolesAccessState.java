package com.example.server.states;

import com.example.users.ServerUser;
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
     * Дозволяє зайти тільки конкретним ролям, враховує ліміт користувачів. У адміністраторів немає привілеїв
     * @param user              користувач, який заходить
     * @param maxUsers          максимальна кількість людей
     * @param activeConnections кількість людей на сервері
     * @return результат
     */
    @Override
    public FtpletResult checkServerAccess(ServerUser user, int maxUsers, AtomicInteger activeConnections) {
        String name = user.getName();
        String role = user.getRoleName();

        boolean isWhiteListed = roleNames.stream()
                .anyMatch(roleName -> roleName.equalsIgnoreCase(role));

        if (isWhiteListed) {
            if (maxUsers == 0) {
                //Виключений ліміт по юзерам
                logger.info(String.format("%s \"%s\" has connected. (%d/%d)",
                        role, name, activeConnections.get(), maxUsers));
                return FtpletResult.DEFAULT;
            }

            if (activeConnections.get() > maxUsers) {
                //Ліміт юзерів вже вийшов
                logger.warn(String.format("%s \"%s\" try to connected, but server is full. (%d/%d)",
                        role, name, activeConnections.get() - 1, maxUsers));
                return FtpletResult.DISCONNECT;
            } else {
                //Ліміт ще дозволяє заходити
                logger.info(String.format("%s \"%s\" has logged in successfully. (%d/%d)",
                        role, name, activeConnections.get(), maxUsers));
                return FtpletResult.DEFAULT;
            }
        }

        //Ролі немає в списку
        logger.warn(String.format("%s \"%s\" try to connected, but this role isn't in whitelist. (%d/%d)",
                role, name, activeConnections.get() - 1, maxUsers));
        return FtpletResult.DISCONNECT;


    }
}
