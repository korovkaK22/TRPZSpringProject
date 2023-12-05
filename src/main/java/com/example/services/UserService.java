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
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private StatesService statesService;
    private PasswordEncryptorImpl passwordEncryptor;


    public List<ServerUser> getAllUsers(){
      //  return userRepository.findAll().stream().map(this::convertUserEntity).toList();
        LinkedList<ServerUser> users = new LinkedList<>();
        users.add(new ServerUser("test", passwordEncryptor.encrypt("test"),new CustomUserStateBuilder(
                "TESTER", "C:\\")
                .setAdmin(false)
                .setEnabled(true)
                .setCanWrite(true)
                .setDownloadSpeed(100_000)
                .build()));
        users.add(new ServerUser("admin", passwordEncryptor.encrypt("admin"),new CustomUserStateBuilder(
                "ADMIN", "C:\\")
                .setAdmin(true)
                .setCanWrite(true)
                .setDownloadSpeed(100_000)
                .build()));
        return users;
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
        if (state.getClass() == CustomUserState.class){
            statesService.save((CustomUserState) state);
        }

    }

}
