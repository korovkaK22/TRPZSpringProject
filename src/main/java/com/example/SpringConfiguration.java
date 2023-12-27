package com.example;

import com.example.server.FTPServer;
import com.example.exceptions.*;
import com.example.server.ServerUserManager;
import com.example.server.states.AllUsersAccessState;
import com.example.server.states.ServerAccessState;
import com.example.services.UserService;
import com.example.templatepattern.AbstractUserManagerInitialization;
import com.example.templatepattern.AllUsersManagerInitialization;
import com.example.templatepattern.OnlyCertainUsersManagerInitialization;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config.properties")
public class SpringConfiguration {

    @Bean(name = "FTPServer")
    public FTPServer getFTPServer(@Value("${server.port}") int port,
                                  @Value("${server.max.users}") int maxServerUsers, ServerUserManager serverUserManager,
                                  ServerAccessState state){

        return new FTPServer(port, maxServerUsers, serverUserManager, state);
    }

    @Bean(name = "serverUserManager")
    public ServerUserManager getConfiguredServerUserManager(@Value("${server.users.whitelist}") boolean isWhitelist,
                                                            @Value("${server.users.whitelist.names}") String  usernames,
                                                            PasswordEncryptor passwordEncryptor,
                                                            UserService userService) {
        AbstractUserManagerInitialization init;
        if (isWhitelist){
            //Тільки адміни зможуть зайти
            String[] names = usernames.trim().split("\\s");
            init = new OnlyCertainUsersManagerInitialization(userService, passwordEncryptor, names);
        } else{
            //Всі люди зможуть зайти
            init = new AllUsersManagerInitialization(userService, passwordEncryptor);
        }
        return init.getInitialisedUserManager();
    }

    @Bean
    public ServerAccessState getServerAccessState(){
        return new AllUsersAccessState();
    }


}
