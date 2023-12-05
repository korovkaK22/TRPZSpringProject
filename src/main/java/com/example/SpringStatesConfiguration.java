package com.example;

import com.example.users.states.AdminUserState;
import com.example.users.states.BannedUserState;
import com.example.users.states.EditorUserState;
import com.example.users.states.ViewerUserState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config.properties")
public class SpringStatesConfiguration {

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
