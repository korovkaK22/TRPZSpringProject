package com.example;

import com.example.dao.UserRepository;
import com.example.server.FTPServer;

import com.example.users.ServerUser;
import com.example.users.ServerUserDirector;
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
