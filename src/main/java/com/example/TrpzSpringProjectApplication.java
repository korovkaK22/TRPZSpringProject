package com.example;

import com.example.mapper.RoleMapper;
import com.example.mapper.UserMapper;
import com.example.repositories.RoleEntityRepository;
import com.example.repositories.UserRepository;
import com.example.security.PasswordEncryptorImpl;
import com.example.server.FTPServer;

import com.example.server.ServerUserManager;
import com.example.services.UserService;
import com.example.users.ServerUser;

import com.example.users.UserRole;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.*;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class TrpzSpringProjectApplication {


    public static void main(String[] args) throws IOException {
        //Поставити обмеження на швидкості користувачам
        Properties properties = new Properties();
        properties.load(new FileReader("src/main/resources/config.properties"));
        int globalUploadSpeed = Integer.parseInt(properties.getProperty("ftp.server.AllUsersUploadSpeed"));
        int globalDownloadSpeed = Integer.parseInt(properties.getProperty("ftp.server.AllUsersDownloadSpeed"));
        UserRole.setGlobalDownloadSpeed(globalDownloadSpeed);
        UserRole.setGlobalUploadSpeed(globalUploadSpeed);

        ConfigurableApplicationContext context = SpringApplication.run(TrpzSpringProjectApplication.class, args);
        // Запустити фтп сервер
        FTPServer server =  context.getBean("FTPServer", FTPServer.class);
        server.start();

//        ServerUserManager manager = context.getBean(ServerUserManager.class);
//        ServerUser user = manager.getServerUserByName("admin").get();
//        List<Authority> auth = user.getAuthorities();
//        auth.remove(user.getAuthorities(TransferRatePermission.class).get(0));
//        auth.add(new TransferRatePermission(0, 0));



        PasswordEncryptorImpl passwordEncryptor =  context.getBean( PasswordEncryptorImpl.class);
        //System.out.println(passwordEncryptor.encrypt("watcher"));


    }

}
