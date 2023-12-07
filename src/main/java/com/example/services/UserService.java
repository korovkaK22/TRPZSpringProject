package com.example.services;

import com.example.entity.UserEntity;
import com.example.entity.UserSnapshotEntity;
import com.example.repositories.UserRepository;
import com.example.repositories.UserSnapshotEntityRepository;
import com.example.security.PasswordEncryptorImpl;
import com.example.server.ServerUserManager;
import com.example.users.ServerUser;
import com.example.exceptions.*;
import com.example.users.states.AbstractUserState;
import com.example.users.states.CustomUserState;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private ApplicationContext applicationContext;
    private UserRepository userRepository;
    private StatesService statesService;
    private PasswordEncryptorImpl passwordEncryptor;
    private UserSnapshotEntityRepository userSnapshotEntityRepository;



    public List<ServerUser> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertUserEntity)
                .sorted(Comparator.comparing(ServerUser::getName))
                .toList();
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

    public void changeUserEntityBySnapshot(UserSnapshotEntity entity){
        UserEntity userEntity = getUserEntityByName(entity.getUsername());
        userEntity.setUsername(entity.getUsername());
        userEntity.setPassword(entity.getPassword());
        userEntity.setStateName(entity.getStateName());
        userRepository.save(userEntity);

    }

    public void updateUserManager(ServerUser user){
      applicationContext.getBean(ServerUserManager.class).save(user);
    }


    public ServerUser getServerUserByName(String name) {
        Optional<UserEntity> userOpt = userRepository.findUserEntityByUsernameIgnoreCase(name);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("Can't find user with name: " + name);
        }
        return convertUserEntity(userOpt.get());
    }

    public UserEntity getUserEntityByName(String name) {
        Optional<UserEntity> userOpt = userRepository.findUserEntityByUsernameIgnoreCase(name);
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("Can't find user with name: " + name);
        }
        return userOpt.get();
    }


    public ServerUser convertUserEntity(UserEntity entity) {
        try {
            return new ServerUser(entity.getUsername(), entity.getPassword(),
                    statesService.getStateByName(entity.getStateName()));
        } catch (Exception e) {
            throw new UserConvertException("Can't convert user " + entity.getUsername() + "\":", e);
        }
    }

    public UserEntity convertServerUser(ServerUser entity) {
        return new UserEntity(entity.getName(), entity.getPassword(), entity.getStateName());
    }

    public void editUser(String name, ServerUser user) {
        //Перевірка, що ім'я не зайняте
        if (!name.equals(user.getName())) {
            if (userRepository.findUserEntityByUsernameIgnoreCase(user.getName()).isPresent()) {
                throw new UserEditException("There is already a user with this name:" + user.getName());
            }
        }

        UserEntity result = convertServerUser(user);
        userRepository.save(result);
        userSnapshotEntityRepository.changeUsernameInAllSnapshot(name, user.getName());

        if (!name.equals(user.getName())) {
            userRepository.deleteById(name);
        }

    }


    public void createUser(ServerUser user) {
        if (userRepository.findUserEntityByUsernameIgnoreCase(user.getName()).isPresent()) {
            throw new UserCreationException("User with this name already exist: " + user.getName());
        }
        userRepository.save(convertServerUser(user));

        AbstractUserState state = user.getState();
        if (state.getClass() == CustomUserState.class) {
            if (!statesService.doesStateExist(state.getName())) {
                statesService.createState((CustomUserState) state);
            }
        }

    }

}
