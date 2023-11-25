package com.example.users;

import com.example.users.states.AdminUserState;
import com.example.users.states.BannedUserState;
import com.example.users.states.EditorUserState;
import com.example.users.states.ViewerUserState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ServerUserDirector {
    ViewerUserState viewerUserState;
    AdminUserState adminUserState;
    BannedUserState bannedUserState;
    EditorUserState editorUserState;
    private ServerUser user = null;

    public ServerUserDirector(ViewerUserState viewerUserState, AdminUserState adminUserState, BannedUserState bannedUserState, EditorUserState editorUserState) {
        this.viewerUserState = viewerUserState;
        this.adminUserState = adminUserState;
        this.bannedUserState = bannedUserState;
        this.editorUserState = editorUserState;
    }

    public void constructDefaultViewerUser(){
        user = new ServerUser("test", "test", viewerUserState);
    }

    public void constructDefaultAdminUser(){
        user = new ServerUser("admin", "admin", adminUserState);
    }

    public void constructDefaultBannedUser(){
        user = new ServerUser("banned", "banned", bannedUserState);
    }

    public void constructDefaultEditorUser(){
        user = new ServerUser("edit", "edit", editorUserState);
    }

    public ServerUser getResult(){
        return user;
    }


}
