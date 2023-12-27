package com.example.services;

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

    public List<ServerUser> getOnlyAdminUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userEntityToServerUser)
                .filter(ServerUser::isAdmin)
                .sorted(Comparator.comparing(ServerUser::getName))
                .toList();
    }

    public  List<ServerUser> getUsersByName(String... names) {
        List<ServerUser> result = new ArrayList<>();
        for (String name : names){
            Optional<UserEntity> userOpt = userRepository.findUserEntityByUsernameIgnoreCase(name);
            if (userOpt.isPresent()){
                result.add(userMapper.userEntityToServerUser(userOpt.get()));
            }   else{
                logger.warn("Can't find such user: "+ name);
            }
        }
        return result;
    }


    /**
     * Перевіряє, чи співпадають паролі;
     *
     * @param name     ім'я користувача
     * @param password пароль
     */
    public boolean checkPasswords(String name, String password) {
        ServerUser user = getServerUserByName(name);
        return passwordEncryptor.matches(password, user.getPassword());
    }



    public void updateUserManager(ServerUser user){
      applicationContext.getBean(ServerUserManager.class).save(user);
    }

    public boolean doesUserExist(String name){
        return  userRepository.findUserEntityByUsernameIgnoreCase(name).isPresent();
    }

    public ServerUser getServerUserByName(String name) {
//        Optional<UserEntity> userOpt = userRepository.findUserEntityByUsernameIgnoreCase(name);
//        if (userOpt.isEmpty()) {
//            throw new UserNotFoundException("Can't find user with name: " + name);
//        }
//        return convertUserEntity(userOpt.get());
        throw new UnsupportedOperationException(); //=====
    }





    public void createUser(String username, String password, String stateName) {
       // UserEntity user = new UserEntity(username,passwordEncryptor.encrypt(password), stateName);

//        if (userRepository.findUserEntityByUsernameIgnoreCase(user.getUsername()).isPresent()) {
//            throw new UserCreationException("User with this name already exist: " + user.getUsername());
//        }
//        userRepository.save(user);
    }

    public void deleteUser(Integer id){
        userRepository.deleteById(id);
    }

}
