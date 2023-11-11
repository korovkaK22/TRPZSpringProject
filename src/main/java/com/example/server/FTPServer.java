package com.example.server;


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
    List<? extends User> userList;
    PasswordEncryptor passwordEncryptor;
    private int maxUsers;
    private AtomicInteger activeConnections = new AtomicInteger(0);
    @Getter
    private  UserManager userManager;
    @Getter
    final FtpServerFactory serverFactory = new FtpServerFactory();
    private static final Logger logger = LoggerFactory.getLogger(FTPServer.class);

    public FTPServer(int port, List<? extends User> userList, PasswordEncryptor passwordEncryptor, int maxUsers) {
        this.port = port;
        this.userList = userList;
        this.passwordEncryptor = passwordEncryptor;
        this.maxUsers = maxUsers;
    }


    public void start() {
        ListenerFactory factory = new ListenerFactory();
        factory.setPort(port);

        ConnectionConfig connectionConfig = new DefaultConnectionConfig(true, 500, maxUsers, 0, 3, 5);
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
        Map<String, Ftplet> m = new HashMap<String, Ftplet>();
        m.put("miaFtplet", new Ftplet() {

            @Override
            public void init(FtpletContext ftpletContext) throws FtpException {
            }

            @Override
            public void destroy() {
            }

            @Override
            public FtpletResult beforeCommand(FtpSession session, FtpRequest request) throws FtpException, IOException {
                return FtpletResult.DEFAULT;
            }

            @Override
            public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply) throws FtpException, IOException {
                //Логін юзера
                if ("PASS".equalsIgnoreCase(request.getCommand()) && reply.getCode() == 230) {
                    logger.info(String.format("User \"%s\" has logged in successfully. (%d/%d)",
                            session.getUser().getName(), activeConnections.incrementAndGet(), maxUsers));
                }
                return FtpletResult.DEFAULT;
            }

            @Override
            public FtpletResult onConnect(FtpSession session) throws FtpException, IOException {
                return FtpletResult.DEFAULT;
            }


            @Override
            public FtpletResult onDisconnect(FtpSession session) throws FtpException, IOException {
                if (session.getUser() != null){
                    String username = session.getUser().getName();
                    logger.info(String.format("User \"%s\" has disconnected. (%d/%d)",
                            username, activeConnections.decrementAndGet(), maxUsers));
                }
                 return FtpletResult.DEFAULT;
            }
        });
        serverFactory.setFtplets(m);
        FtpServer server = serverFactory.createServer();
        try {
            server.start();
            logger.info(String.format("FTP Server was started at port %d", port));
        } catch (FtpException ex) {
            logger.error("CRASH: " + ex.getMessage());
        }
    }
}


