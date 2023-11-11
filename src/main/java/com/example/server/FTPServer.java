package com.example.server;


import com.example.server.exceptions.ServerInitializationException;
import com.example.users.ServerUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.ftpserver.ConnectionConfig;
import org.apache.ftpserver.DataConnectionConfiguration;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.impl.DefaultConnectionConfig;
import org.apache.ftpserver.impl.DefaultDataConnectionConfiguration;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class FTPServer {
    //todo валідатор вхідних даних

    private final int port;
    List<ServerUser> userList;
    PasswordEncryptor passwordEncryptor;
    private final int maxUsers;
    private final AtomicInteger activeConnections = new AtomicInteger(0);
    @Getter
    private UserManager userManager;
    FtpServer server;
    private static final Logger logger = LoggerFactory.getLogger(FTPServer.class);

    public FTPServer(int port, List<ServerUser> userList, PasswordEncryptor passwordEncryptor, int maxUsers) throws ServerInitializationException {
        try {
            FTPServerValidator.validate(port, userList, passwordEncryptor, maxUsers);
        } catch (ServerInitializationException e) {
            logger.error(e.getMessage());
            throw e;
        }
        this.port = port;
        this.userList = userList;
        this.passwordEncryptor = passwordEncryptor;
        this.maxUsers = maxUsers;
        init();
    }

    private void init() {
        ListenerFactory factory = new ListenerFactory();
        factory.setPort(port);

        ConnectionConfig connectionConfig = new DefaultConnectionConfig(false, 500, 0, 0, 3, 5);
        FtpServerFactory serverFactory = new FtpServerFactory();
        serverFactory.setConnectionConfig(connectionConfig);

        serverFactory.addListener("default", factory.createListener());
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setPasswordEncryptor(passwordEncryptor);
        userManager = userManagerFactory.createUserManager();
        try {
            for (User user : userList) {
                userManager.save(user);
            }
        } catch (FtpException e1) {

        }
        serverFactory.setUserManager(userManager);
        Map<String, Ftplet> m = new HashMap<>();
        m.put("miaFtplet", new FtpletImpl());
        serverFactory.setFtplets(m);
        server = serverFactory.createServer();
    }


    public void start() {
        try {
            server.start();
            logger.info(String.format("FTP Server was started at port %d, (0/%d)", port, maxUsers));
        } catch (FtpException ex) {
            logger.error("CRASH: " + ex.getMessage());
        }
    }


    private class FtpletImpl implements Ftplet {
        //Коли юзера не пускає на сервер, бо сервер забитий,  onDisconnect глушиться
        boolean silentMode = false;

        @Override
        public void init(FtpletContext ftpletContext) throws FtpException {
        }

        @Override
        public void destroy() {
        }

        @Override
        public FtpletResult beforeCommand(FtpSession ftpSession, FtpRequest ftpRequest) throws FtpException, IOException {
            return FtpletResult.DEFAULT;
        }

        @Override
        public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply) throws FtpException, IOException {

            //Логін юзера пройшов добре
            if ("PASS".equalsIgnoreCase(request.getCommand()) && reply.getCode() == 230) {
                return tryToJoin(session);
            }

            return FtpletResult.DEFAULT;
        }

        @Override
        public FtpletResult onConnect(FtpSession session) throws FtpException, IOException {
            activeConnections.incrementAndGet();
            return FtpletResult.DEFAULT;
        }


        @Override
        public FtpletResult onDisconnect(FtpSession session) throws FtpException, IOException {
            activeConnections.decrementAndGet();
            //якщо не сайлент мод, то юзер вийшов з серверу
            if (!silentMode) {
                String userName = session.getUser().getName();
                logger.info(String.format("%s \"%s\" has disconnected in successfully. (%d/%d)",
                        getUser(userName).getStateName(), userName, activeConnections.get(), maxUsers));
            } else {
                silentMode = false;
            }
            return FtpletResult.DEFAULT;
        }

        //Визивається тільки тоді, коли вже все добре пройшло
        private FtpletResult tryToJoin(FtpSession session) throws FtpException, IOException {
            String userName = session.getUser().getName();
            ServerUser user = getUser(userName);

            //Виключений ліміт по юзерам
            if (maxUsers == 0) {
                logger.info(String.format("%s \"%s\" has connected. (%d/%d)",
                        user.getStateName(), userName, activeConnections.get(), maxUsers));
                return FtpletResult.DEFAULT;
            }
            //Ліміт юзерів вже вийшов
            if (activeConnections.get() > maxUsers) {
                //Це адмін
                if (isAdmin(userName)) {
                    logger.info(String.format("%s \"%s\" has connected. (%d/%d)",
                            user.getStateName(), userName, activeConnections.get(), maxUsers));
                    return FtpletResult.DEFAULT;
                } else {
                    //Це не адмін, кік
                    logger.warn(String.format("%s \"%s\" try to connected, but server is full. (%d/%d)",
                            user.getStateName(), userName, activeConnections.get() - 1, maxUsers));
                    session.write(new DefaultFtpReply(FtpReply.REPLY_421_SERVICE_NOT_AVAILABLE_CLOSING_CONTROL_CONNECTION,
                            String.format("Maximum number of sessions reached (%d/%d).", activeConnections.get() - 1, maxUsers)));
                    silentMode = true;
                    return FtpletResult.DISCONNECT;
                }
            } else {
                //Ліміт ще дозволяє заходити
                logger.info(String.format("%s \"%s\" has logged in successfully. (%d/%d)",
                        user.getStateName(), session.getUser().getName(), activeConnections.get(), maxUsers));
                return FtpletResult.DEFAULT;
            }
        }

        //Отримати по імені юзераз юзерлісту
        private ServerUser getUser(String username) {
            Optional<ServerUser> result = userList.stream()
                    .filter(o -> username.equals(o.getName()))
                    .findFirst();
            if (result.isEmpty()) {
                throw new NullPointerException("Unknown User");
            }
            return result.get();
        }

        private boolean isAdmin(String username) {
            return userList.stream()
                    .filter(o -> username.equals(o.getName()))
                    .findFirst()
                    .map(ServerUser::isAdmin)
                    .orElse(false);
        }
    }

    private static class FTPServerValidator {
        public static void validate(int port, List<ServerUser> userList, PasswordEncryptor passwordEncryptor, int maxUsers) throws ServerInitializationException {
            // Перевірка порту
            if (port <= 0 || port > 65535) {
                throw new ServerInitializationException("Invalid port number: " + port + ". Port number must be between 1 and 65535.");
            }

            // Перевірка списку користувачів
            if (userList == null || userList.isEmpty()) {
                throw new ServerInitializationException("User list cannot be null or empty.");
            }

            // Перевірка шифрувальника паролів
            if (passwordEncryptor == null) {
                throw new ServerInitializationException("Password encryptor cannot be null.");
            }

            // Перевірка максимальної кількості користувачів
            if (maxUsers <= 0) {
                throw new ServerInitializationException("Maximum number of users must be greater than 0.");
            }
        }
    }


}



