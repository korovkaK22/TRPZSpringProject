package com.example.dao;

import com.example.users.ServerUser;
import com.example.users.ServerUserDirector;
import com.example.users.states.CustomUserState;
import com.example.users.states.CustomUserStateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class UserRepository {
   List<ServerUser> users = new LinkedList<>();
   private final ServerUserDirector director;

   @Autowired
    public UserRepository(ServerUserDirector director) {
       this.director = director;
    }

    public void addUser(ServerUser user){
       users.add(user);
   }

    public List<ServerUser> getUsers(){

        director.constructDefaultViewerUser();
        ServerUser defaultViewerUser =  director.getResult();
        addUser(defaultViewerUser);

        director.constructDefaultAdminUser();
        ServerUser defaultAdminUser =  director.getResult();
        addUser(defaultAdminUser);

        director.constructDefaultBannedUser();
        ServerUser defaultBannedUser =  director.getResult();
        addUser(defaultBannedUser);

        director.constructDefaultEditorUser();
        ServerUser defaultEditorUser =  director.getResult();
        addUser(defaultEditorUser);



        CustomUserState testerState = new CustomUserStateBuilder(
                "TESTER", "C:\\Users\\sa095\\Desktop\\tests")
                .setAdmin(true)
                .setCanWrite(true)
                .setDownloadSpeed(100_000)
                .build();
        ServerUser user = new ServerUser("tester", "tester", testerState);

        addUser(user);

        return users;
        }


}
