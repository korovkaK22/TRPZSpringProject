package com.example;

import com.example.security.PasswordEncryptorImpl;
import com.example.server.FTPServer;
import com.example.exceptions.*;
import com.example.server.ServerUserManager;
import com.example.services.UserService;
import com.example.users.ServerUser;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config.properties")
public class SpringConfiguration {

    @Bean(name = "FTPServer")
    public FTPServer getFTPServer(@Value("${serverPort}") int port,
                                  @Value("${maxServerUsers}") int maxServerUsers, ServerUserManager serverUserManager) throws ServerInitializationException {

        return new FTPServer(port, maxServerUsers, serverUserManager);
    }

    @Bean(name = "serverUserManager")
    public ServerUserManager getConfiguredServerUserManager(PasswordEncryptor passwordEncryptor, UserService userService){
        ServerUserManager manager = new ServerUserManager(passwordEncryptor);
        manager.save(userService.getAllUsers());
        return manager;
    }


}
