package com.example;

import com.example.dao.UserRepository;
import com.example.security.PasswordEncryptorImpl;
import com.example.server.FTPServer;
import com.example.users.states.AdminUserState;
import com.example.users.states.BannedUserState;
import com.example.users.states.DefaultUserState;
import com.example.users.states.PremiumUserState;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config.properties")
public class SpringConfiguration {

    @Bean(name = "FTPServer")
    public FTPServer getFTPServer(@Value("${serverPort}") int port, UserRepository userRepository, @Value("${maxServerUsers}") int maxServerUsers){
        return new FTPServer(port,userRepository.getUsers(), getPasswordEncryptor(), maxServerUsers);
    }


    @Bean
    public PasswordEncryptor getPasswordEncryptor(){
        return new PasswordEncryptorImpl();
    }

    @Bean
    public AdminUserState adminUserState(@Value("${adminUsersPath}") String userPath){
        return new AdminUserState(true, userPath, true, 2000000, 2000000, true);
    }

    @Bean
    public DefaultUserState defaultUserState(@Value("${defaultUsersPath}") String userPath){
        return new DefaultUserState(true, userPath, false, 2000, 2000, false);
    }

    @Bean
    public PremiumUserState premiumUserState(@Value("${premiumUsersPath}") String userPath){
        return new PremiumUserState(true, userPath , false, 20000, 20000, true);
    }

    @Bean
    public BannedUserState bannedUserState(@Value("${defaultUsersPath}") String userPath){
        return new BannedUserState(false, userPath, false, 0, 0, false);
    }

}
