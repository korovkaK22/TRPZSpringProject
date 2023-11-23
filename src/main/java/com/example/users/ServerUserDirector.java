package com.example.users;

import com.example.users.states.AdminUserState;
import com.example.users.states.BannedUserState;
import com.example.users.states.EditorUserState;
import com.example.users.states.ViewerUserState;
import org.springframework.stereotype.Component;

@Component
public record ServerUserDirector(ServerUserBuilder builder, ViewerUserState viewerUserState,
                                 AdminUserState adminUserState, BannedUserState bannedUserState, EditorUserState editorUserState) {

    public void constructDefaultViewerUser(){
        builder.setName("test").setPassword("test").setState(viewerUserState).build();
    }

    public void constructDefaultAdminUser(){
        builder.setName("admin").setPassword("admin").setState(adminUserState).build();
    }

    public void constructDefaultBannedUser(){
        builder.setName("banned").setPassword("banned").setState(bannedUserState).build();
    }

    public void constructDefaultEditorUser(){
        builder.setName("edit").setPassword("edit").setState(editorUserState).build();
    }

    public ServerUser getResult(){
        return builder.build();
    }


}
