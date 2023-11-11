package com.example.dao;

import com.example.users.ServerUser;
import com.example.users.ServerUserBuilder;
import com.example.users.states.CustomUserStateBuilder;
import com.example.users.states.AdminUserState;
import com.example.users.states.BannedUserState;
import com.example.users.states.ViewerUserState;
import com.example.users.states.EditorUserState;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@Setter
public class UserRepository {
    @Autowired
    ViewerUserState viewerUserState;
    @Autowired
    AdminUserState adminUserState;
    @Autowired
    BannedUserState bannedUserState;
    @Autowired
    EditorUserState editorUserState;


    public List<ServerUser> getUsers(){
        List<ServerUser> result = new LinkedList<>();
        result.add(new ServerUserBuilder().setName("test").setPassword("test").setState(viewerUserState).build());
        result.add(new ServerUserBuilder().setName("edit").setPassword("edit").setState(editorUserState).build());
        result.add(new ServerUserBuilder().setName("banned").setPassword("banned").setState(bannedUserState).build());
        return result;
        }


}
