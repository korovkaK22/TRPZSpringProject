package com.example;

import com.example.server.FTPServer;
import com.example.exceptions.*;
import com.example.server.ServerUserManager;
import com.example.server.states.AllUsersAccessState;
import com.example.server.states.NoNewUsersState;
import com.example.server.states.OnlyCertainRolesAccessState;
import com.example.server.states.ServerAccessState;
import com.example.services.UserService;
import com.example.templatepattern.AbstractUserManagerInitialization;
import com.example.templatepattern.AllUsersManagerInitialization;
import com.example.templatepattern.OnlyCertainUsersManagerInitialization;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Locale;

@Configuration
@PropertySource("classpath:config.properties")
public class SpringConfiguration {
    private static final Logger logger = LogManager.getRootLogger();
    @Autowired
    private Environment env;

    @Bean(name = "FTPServer")
    public FTPServer getFTPServer(@Value("${ftp.server.port}") int port,
                                  @Value("${ftp.server.max.users}") int maxServerUsers,
                                  ServerUserManager serverUserManager,
                                  ServerAccessState state,
                                  @Value("${ftp.server.max.users}") int globalUploadSpeed,
                                  @Value("${ftp.server.max.users}") int globalDownloadSpeed){

        return new FTPServer(port, maxServerUsers, serverUserManager, state, globalUploadSpeed, globalDownloadSpeed);
    }

    @Bean(name = "serverUserManager")
    public ServerUserManager getConfiguredServerUserManager(@Value("${ftp.server.users.whitelist}") boolean isWhitelist,
                                                            PasswordEncryptor passwordEncryptor,
                                                            UserService userService) throws ServerUserInitializationException {
        AbstractUserManagerInitialization init;
        if (isWhitelist){
            String usernames = env.getProperty("ftp.server.users.whitelist.names");
            if (usernames == null){
                String errorMessage ="There is no field \"ftp.server.users.whitelist.names\"";
                logger.error(errorMessage);
                throw new ServerUserInitializationException(errorMessage);
            }
            //Тільки адміни зможуть зайти
            logger.info("Whitelist turned on: "+ usernames);
            String[] names = usernames.trim().split("\\s");
            init = new OnlyCertainUsersManagerInitialization(userService, passwordEncryptor, names);
        } else{
            //Всі люди зможуть зайти
            logger.info("Whitelist turned off");
            init = new AllUsersManagerInitialization(userService, passwordEncryptor);
        }
        return init.getInitialisedUserManager();
    }

    @Bean
    public ServerAccessState getServerAccessState(@Value("${ftp.server.state}") String state) throws ServerAccessStateInitException {
        state = state.toLowerCase(Locale.ROOT);
        return switch (state){
            case "allusers" -> {
                logger.info("ServerState: AllUsersAccessState");
                yield new AllUsersAccessState();}
            case "nonewusers" -> {
                logger.info("ServerState: noNewUsers");
                yield new NoNewUsersState();}
            case "onlycertainroles" -> {
                String rolesStr = env.getProperty("ftp.server.roles");
                if (rolesStr == null){
                    String errorMessage ="There is no field \"ftp.server.roles\"";
                    logger.error(errorMessage);
                    throw new ServerAccessStateInitException(errorMessage);
                }
                logger.info("ServerState: OnlyCertainRolesAccessState, roles: "+ rolesStr);
                String[] roles = rolesStr.trim().split("\\s");
                yield new OnlyCertainRolesAccessState(List.of(roles));
            }
            default -> throw new ServerAccessStateInitException("There is no such state: "+ state);
        };

    }


}
