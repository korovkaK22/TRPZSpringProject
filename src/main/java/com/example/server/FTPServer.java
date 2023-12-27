package com.example.server;


import com.example.exceptions.*;
import com.example.server.states.ServerAccessState;
import com.example.users.ServerUser;
import com.example.visitor.IVisitor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.ftpserver.ConnectionConfig;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.impl.DefaultConnectionConfig;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Getter
public class FTPServer {
    private final int port;
    private final int maxUsers;
    private final AtomicInteger activeConnections = new AtomicInteger(0);
    private final Set<ServerUser> activeUsers = Collections.synchronizedSet(new HashSet<>());
    private final ServerAccessState state;


    private final ServerUserManager userManager;
    private DefaultFtpServer server;
    private static final Logger logger = LoggerFactory.getLogger(FTPServer.class);

    public FTPServer(int port, int maxUsers, ServerUserManager userManager, ServerAccessState state){
        this.port = port;
        this.maxUsers = maxUsers;
        this.userManager = userManager;
        this.state = state;
        init();
    }

    private void init() {
        ListenerFactory factory = new ListenerFactory();
        factory.setPort(port);

        ConnectionConfig connectionConfig = new DefaultConnectionConfig(false, 500,
                    0, 0, 3, 5);
        FtpServerFactory serverFactory = new FtpServerFactory();
        serverFactory.setConnectionConfig(connectionConfig);

        serverFactory.addListener("default", factory.createListener());

        serverFactory.setUserManager(userManager);
        Map<String, Ftplet> m = new HashMap<>();
        m.put("miaFtplet", new FtpletImpl());
        serverFactory.setFtplets(m);
        server =(DefaultFtpServer) serverFactory.createServer();
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

                String userName = session.getUser().getName();
                ServerUser user = getUser(userName);
                logger.info(String.format("%s \"%s\" has disconnected in successfully. (%d/%d)",
                        getUser(userName).getStateName(), userName, activeConnections.get(), maxUsers));
                activeUsers.remove(user);
            return FtpletResult.DEFAULT;
        }

        //Визивається тільки тоді, коли вже все добре пройшло
        private FtpletResult tryToJoin(FtpSession session) throws FtpException, IOException {
            String userName = session.getUser().getName();
            ServerUser user = getUser(userName);

            FtpletResult result =  state.checkServerAccess(user, maxUsers, activeConnections);
            //Користувача пустили на сервер
            if (result == FtpletResult.DEFAULT){
                activeUsers.add(user);
            }
            return result;
        }

        //Отримати по імені юзера з юзерлісту
        private ServerUser getUser(String username) {
           return userManager.getServerUserByName(username)
                   .orElseThrow(() -> new NullPointerException("Unknown User"));

        }

    }

    public void accept(IVisitor visitor){
        visitor.getStatistic(this);
    }


}



