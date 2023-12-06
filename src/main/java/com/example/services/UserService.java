package com.example.services;

import com.example.entity.UserEntity;
import com.example.repositories.UserRepository;
import com.example.security.PasswordEncryptorImpl;
import com.example.users.ServerUser;
import com.example.exceptions.*;
import com.example.users.states.AbstractUserState;
import com.example.users.states.CustomUserState;
import com.example.users.states.CustomUserStateBuilder;
import lombok.AllArgsConstructor;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private StatesService statesService;
    private PasswordEncryptorImpl passwordEncryptor;

    public List<ServerUser> getAllUsers(){
       return userRepository.findAll().stream()
               .map(this::convertUserEntity)
               .sorted(Comparator.comparing(ServerUser::getName))
               .toList();
    }

    /**
     * Перевіряє, чи співпадають паролі;
     * @param name ім'я користувача
     * @param password пароль
     */
    public boolean checkPasswords(String name, String password){
        ServerUser user = getServerUserByName(name);
       return passwordEncryptor.matches(password, user.getPassword());
    }



    public ServerUser getServerUserByName(String name){
        Optional<UserEntity> userOpt = userRepository.findUserEntityByUsernameIgnoreCase(name);
        if (userOpt.isEmpty()){
            throw new UserNotFoundException("Can't find user with name: "+ name);
        }
       return convertUserEntity(userOpt.get());
    }




    public ServerUser convertUserEntity(UserEntity entity){
        try {
           return new ServerUser(entity.getUsername(), entity.getPassword(),
                    statesService.getStateByName(entity.getStateName()));
        } catch (Exception e){
            throw new UserConvertException("Can't convert user "+ entity.getUsername()+"\":", e);
        }
    }



    public void save(ServerUser user){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getName());
        userEntity.setPassword(user.getPassword());
        AbstractUserState state = user.getState();
        userEntity.setStateName(state.getName());
        userRepository.save(userEntity);
        if (state.getClass() == CustomUserState.class){
            statesService.save((CustomUserState) state);
        }

    }

}
