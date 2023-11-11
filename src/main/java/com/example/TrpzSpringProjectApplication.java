package com.example;

import com.example.dao.UserRepository;
import com.example.server.FTPServer;
import com.example.users.states.DefaultUserState;

import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class TrpzSpringProjectApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TrpzSpringProjectApplication.class, args);
        UserRepository userRepository = context.getBean("userRepository", UserRepository.class);
        FTPServer server =  context.getBean("FTPServer", FTPServer.class);
        server.start();

//        String[] array = context.getBeanDefinitionNames();
//        Arrays.sort(array);
//        String s = Arrays.toString(array);
//        s = s.replaceAll(", ", ",\n");
//        System.out.println(s);
    }

}
