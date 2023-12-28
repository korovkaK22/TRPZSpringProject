package com.example.services;

import com.example.entity.RoleEntity;
import com.example.mapper.RoleMapper;
import com.example.repositories.RoleEntityRepository;
import com.example.server.ServerUserManager;
import com.example.users.ServerUser;
import com.example.users.UserRole;
import com.example.exceptions.*;
import com.example.users.UserRoleEntityBuilder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@AllArgsConstructor
public class RoleService {
    private ApplicationContext applicationContext;
    private RoleEntityRepository roleEntityRepository;
    private RoleMapper roleMapper;


    public List<UserRole> getAllRoles(){
      return  roleEntityRepository.findAll().stream().map(roleMapper::roleEntityToUserRole).toList();
    }

    public Optional<RoleEntity> getRoleEntity(int id){
       return roleEntityRepository.getById(id);
    }

    public Optional<UserRole> getUserRole(int id){
        return roleEntityRepository.getById(id).map(roleMapper::roleEntityToUserRole);
    }

    public int createRole(String name, String directory, boolean canWrite, boolean isAdmin,
                          boolean isEnabled, int downloadSpeed, int uploadSpeed){
        RoleEntity result = new UserRoleEntityBuilder(name, directory)
                .setCanWrite(canWrite)
                .setAdmin(isAdmin)
                .setEnabled(isEnabled)
                .setDownloadSpeed(downloadSpeed)
                .setUploadSpeed(uploadSpeed)
                .build();

        return roleEntityRepository.save(result).getId();
    }

    public void editRole(int id, String name, String directory, boolean canWrite, boolean isAdmin,
                         boolean isEnabled, int downloadSpeed, int uploadSpeed){
         Optional<RoleEntity> entityOpt = getRoleEntity(id);
         if (entityOpt.isEmpty()){
             return;
         }
         RoleEntity entity = entityOpt.get();
         entity.setName(name);
         entity.setPath(directory);
         entity.setCanWrite(canWrite);
         entity.setIsAdmin(isAdmin);
         entity.setIsEnabled(isEnabled);
         entity.setDownloadSpeed(downloadSpeed);
         entity.setUploadSpeed(uploadSpeed);

         roleEntityRepository.save(entity);

         //Обновляю список на сервері одразу
         getManager().getUsers().values().stream()
                 .map(u -> ((ServerUser) u))
                 .filter(u -> u.getRole().getId() == id)
                 .forEach(u -> u.setRole(getUserRole(id).get()));
    }

    private ServerUserManager getManager(){
        return  applicationContext.getBean(ServerUserManager.class);
    }

}
