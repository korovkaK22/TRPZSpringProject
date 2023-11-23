package com.example;

import com.example.dao.UserRepository;
import com.example.security.PasswordEncryptorImpl;
import com.example.server.FTPServer;
import com.example.server.exceptions.ServerInitializationException;
import com.example.users.ServerUserBuilder;
import com.example.users.ServerUserBuilderImpl;
import com.example.users.states.*;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config.properties")
public class SpringConfiguration {

    @Bean(name = "FTPServer")
    public FTPServer getFTPServer(@Value("${serverPort}") int port, UserRepository userRepository,
                                  @Value("${maxServerUsers}") int maxServerUsers) throws ServerInitializationException {
        return new FTPServer(port,userRepository.getUsers(), getPasswordEncryptor(), maxServerUsers);
    }

    @Bean
    public ServerUserBuilder getServerUserBuilder(){
        return new ServerUserBuilderImpl();
    }


    @Bean
    public PasswordEncryptor getPasswordEncryptor(){
        return new PasswordEncryptorImpl();
    }

    @Bean
    public AdminUserState adminUserState(@Value("${adminUsersPath}") String userPath){
        return new AdminUserState(true, userPath, true, 2_000_000, 2_000_000, true, "ADMIN");
    }

    @Bean
    public ViewerUserState viewerUserState(@Value("${defaultUsersPath}") String userPath){
        return new ViewerUserState(true, userPath, false, 200_000, 200_000, false, "USER");
    }

    @Bean
    public EditorUserState editorUserState(@Value("${premiumUsersPath}") String userPath){
        return new EditorUserState(true, userPath , false, 200_000, 200_000, true, "EDITOR");
    }

    @Bean
    public BannedUserState bannedUserState(@Value("${defaultUsersPath}") String userPath){
        return new BannedUserState(false, userPath, false, 0, 0, false, "BANNED");
    }

}
