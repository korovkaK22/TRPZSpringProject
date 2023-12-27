package com.example.services;

import com.example.entity.RoleEntity;
import com.example.entity.UserEntity;
import com.example.mapper.UserMapper;
import com.example.repositories.UserRepository;

import com.example.security.PasswordEncryptorImpl;
import com.example.server.ServerUserManager;
import com.example.users.ServerUser;
import com.example.exceptions.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private static final Logger logger = LogManager.getRootLogger();
    private ApplicationContext applicationContext;
    private UserRepository userRepository;
    private RoleService roleService;
    private PasswordEncryptorImpl passwordEncryptor;
    private UserMapper userMapper;


    public List<ServerUser> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userEntityToServerUser)
                .sorted(Comparator.comparing(ServerUser::getName))
                .toList();

    }


    public List<ServerUser> getUsersByName(String... names) {
        List<ServerUser> result = new ArrayList<>();
        for (String name : names) {
            Optional<UserEntity> userOpt = userRepository.findUserEntityByUsernameIgnoreCase(name);
            if (userOpt.isPresent()) {
                result.add(userMapper.userEntityToServerUser(userOpt.get()));
            } else {
                logger.warn("Can't find such user: " + name);
            }
        }
        return result;
    }


    /**
     * Перевіряє, чи співпадають паролі;
     * @param name     ім'я користувача
     * @param password пароль
     */
    public boolean checkPasswords(String name, String password) {
        Optional<ServerUser> userOpt = getServerUserByName(name);
        if (userOpt.isEmpty()) {
            return false;
        }
        return passwordEncryptor.matches(password, userOpt.get().getPassword());

    }


    public boolean doesUserExist(String name) {
        return userRepository.findUserEntityByUsernameIgnoreCase(name).isPresent();
    }
    public Optional<ServerUser> getServerUserById(int id) {
        Optional<UserEntity> userOpt = userRepository.findUserEntityById(id);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userMapper.userEntityToServerUser(userOpt.get()));
    }


    public Optional<ServerUser> getServerUserByName(String name) {
        Optional<UserEntity> userOpt = userRepository.findUserEntityByUsernameIgnoreCase(name);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userMapper.userEntityToServerUser(userOpt.get()));
    }

    /**
     * Створює юзера і повертає його id
     * @param username ім'я
     * @param password пароль
     * @param roleId id ролі
     * @return id юзера або -1, при нестворенні
     */
    public int createUser(String username, String password, int roleId) {
         UserEntity user = new UserEntity();
         user.setUsername(username);
         user.setPassword(passwordEncryptor.encrypt(password));
         Optional<RoleEntity> entityOpt = roleService.getRoleEntity(roleId);
         if (entityOpt.isPresent()){
             user.setRole(entityOpt.get());
             UserEntity entity= userRepository.save(user);
             return entity.getId();
         }
        return -1;
    }

    public int getUserId(String name){
        Optional<UserEntity> userOpt = userRepository.findUserEntityByUsernameIgnoreCase(name);
        if (userOpt.isEmpty()){
            return -1;
        }
        return userOpt.get().getId();
    }

    public boolean changeRole(int userId, int newRoleId){
        Optional<UserEntity> entityOpt = userRepository.findUserEntityById(userId);
        if (entityOpt.isEmpty()){
            return false;
        }
        UserEntity user = entityOpt.get();
        Optional<RoleEntity> roleEntityOpt = roleService.getRoleEntity(newRoleId);
        if (roleEntityOpt.isEmpty()){
            return false;
        }
        RoleEntity role = roleEntityOpt.get();
        user.setRole(role);

        ServerUserManager manager = getUserManager();
        Optional<ServerUser> serverUserOpt= manager.getServerUserByName(user.getUsername());
        serverUserOpt.ifPresent(serverUser -> serverUser.setRole(roleService.getUserRole(newRoleId).get()));
        return true;
    }

    private ServerUserManager getUserManager(){
        return  applicationContext.getBean(ServerUserManager.class);
    }

    public void deleteUser(Integer id) {
        String name = userRepository.getReferenceById(id).getUsername();
        userRepository.deleteById(id);
        getUserManager().delete(name);
    }

}
