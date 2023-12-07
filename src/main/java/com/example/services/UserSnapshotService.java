package com.example.services;

import com.example.entity.UserSnapshotEntity;
import com.example.repositories.UserSnapshotEntityRepository;
import com.example.users.ServerUser;
import com.example.users.ServerUserSnapshot;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.exceptions.*;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserSnapshotService {
    private UserSnapshotEntityRepository snapshotRepository;
    private StatesService statesService;
    private UserService userService;

//    Цю штуку треба буде перевірити
    public void setToUserSnapshots(UserSnapshotEntity entity){
        userService.changeUserEntityBySnapshot(entity);
        userService.updateUserManager(userService.getServerUserByName(entity.getUsername()));
    }



    public List<UserSnapshotEntity> getSnapshotsByUser(String username) {
        return snapshotRepository.findAllByUsername(username);
    }

    public ServerUserSnapshot convertSnapshot(UserSnapshotEntity entity) {
        return new ServerUserSnapshot(entity.getUsername(), entity.getPassword(),
                statesService.getStateByName(entity.getStateName()));
    }

    public void changeUsernameInAllSnapshot(String oldUsername, String newUsername){
            snapshotRepository.changeUsernameInAllSnapshot(oldUsername, newUsername);
    }

    public void saveSnapshot(ServerUserSnapshot snapshot, String name) {
        snapshotRepository.save(new UserSnapshotEntity(name, snapshot));
    }

    public void saveSnapshot(ServerUser user, String name) {
        snapshotRepository.save(new UserSnapshotEntity(name, user.createSnapshot()));
    }
    public UserSnapshotEntity getSnapshotById(int id) {
        Optional<UserSnapshotEntity> entity = snapshotRepository.findById(id);
        if (entity.isEmpty()){
            throw new SnapshotNotFoundException("There is no such snapshot with this id: "+ id);
        }
        return entity.get();
    }

    public void deleteSnapshot(int id){
        snapshotRepository.deleteById(id);
    }

    public ServerUserSnapshot createSnapshot(ServerUser user){
        return user.createSnapshot();
    }

}
