package com.example.users.states;


import com.example.entity.CustomStateEntity;

public class CustomUserState extends AbstractUserState{

    public CustomUserState(boolean isEnabled, String homeDir, boolean isAdmin,
                           int uploadSpeed, int downloadSpeed, boolean canWrite, String name) {
        super(isEnabled, homeDir, isAdmin, uploadSpeed, downloadSpeed, canWrite, name);
    }


    public CustomUserState(CustomStateEntity entity) {
      this(entity.isEnabled(), entity.getDirection(), entity.isAdmin(), entity.getUploadSpeed(),
              entity.getDownloadSpeed(), entity.isCanWrite(), entity.getName());
    }

}
