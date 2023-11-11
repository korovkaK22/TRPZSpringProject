package com.example.server;


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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class FTPServer {
    //todo валідатор вхідних даних

    private int port;
    List<ServerUser> userList;
    PasswordEncryptor passwordEncryptor;
    private int maxUsers;
    private AtomicInteger activeConnections = new AtomicInteger(0);
    @Getter
    private UserManager userManager;
    @Getter
    final FtpServerFactory serverFactory = new FtpServerFactory();
    private static final Logger logger = LoggerFactory.getLogger(FTPServer.class);

    public FTPServer(int port, List<ServerUser> userList, PasswordEncryptor passwordEncryptor, int maxUsers) {
        this.port = port;
        this.userList = userList;
        this.passwordEncryptor = passwordEncryptor;
        this.maxUsers = maxUsers;
    }


    public void start() {
        ListenerFactory factory = new ListenerFactory();
        factory.setPort(port);

        ConnectionConfig connectionConfig = new DefaultConnectionConfig(false, 500, 0, 0, 3, 5);
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
        FtpServer server = serverFactory.createServer();
        try {
            server.start();
            logger.info(String.format("FTP Server was started at port %d (0/%d)", port, maxUsers));
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
            //сайлент мод або юзер вийшов з серверу
            if (!silentMode) {
                logger.info(String.format("User \"%s\" has disconnected in successfully. (%d/%d)",
                        session.getUser().getName(), activeConnections.get(), maxUsers));
            } else {
                silentMode = false;
            }
            return FtpletResult.DEFAULT;
        }

        //Визивається тільки тоді, коли вже все добре пройшло
        private FtpletResult tryToJoin(FtpSession session) throws FtpException, IOException {
            String userName = session.getUser().getName();

            //Виключений ліміт по юзерам
            if (maxUsers == 0) {
                logger.info(String.format("User \"%s\" has connected. (%d/%d)",
                        session.getUser().getName(), activeConnections.get(), maxUsers));
                return FtpletResult.DEFAULT;
            }
            //Ліміт юзерів вже вийшов
            if (activeConnections.get() > maxUsers) {
                //Це адмін
                if (isAdmin(userName)) {
                    logger.info(String.format("User \"%s\" has connected. (%d/%d)",
                            session.getUser().getName(), activeConnections.get(), maxUsers));
                    return FtpletResult.DEFAULT;
                } else {
                    session.write(new DefaultFtpReply(FtpReply.REPLY_421_SERVICE_NOT_AVAILABLE_CLOSING_CONTROL_CONNECTION,
                             String.format("Maximum number of sessions reached (%d/%d).",activeConnections.get(), maxUsers)));
                    silentMode = true;
                    return FtpletResult.DISCONNECT;
                }
            } else {
                //Ліміту ще немає
                logger.info(String.format("User \"%s\" has logged in successfully. (%d/%d)",
                        session.getUser().getName(), activeConnections.get(), maxUsers));
                return FtpletResult.DEFAULT;
            }
        }


        private boolean isAdmin(String username) {
            return userList.stream()
                    .filter(o -> username.equals(o.getName()))
                    .findFirst()
                    .map(ServerUser::isAdmin) // перетворюємо Optional<ServerUser> в Optional<Boolean>
                    .orElse(false);
        }
    }

}



