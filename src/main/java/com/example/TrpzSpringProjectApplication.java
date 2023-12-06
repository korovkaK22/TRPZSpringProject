package com.example;

import com.example.security.PasswordEncryptorImpl;
import com.example.server.FTPServer;

import com.example.services.UserService;
import com.example.users.ServerUser;
import com.example.users.states.AdminUserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TrpzSpringProjectApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TrpzSpringProjectApplication.class, args);
        FTPServer server =  context.getBean("FTPServer", FTPServer.class);
        server.start();




//        String[] array = context.getBeanDefinitionNames();
//        Arrays.sort(array);
//        String s = Arrays.toString(array);
//        s = s.replaceAll(", ", ",\n");
//        System.out.println(s);
    }

}
