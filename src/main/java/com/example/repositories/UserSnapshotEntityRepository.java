package com.example.repositories;

import com.example.entity.UserSnapshotEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSnapshotEntityRepository extends JpaRepository<UserSnapshotEntity, Integer> {

    List<UserSnapshotEntity> findAllByUsername(String username);



    @Modifying
    @Transactional
    @Query("UPDATE UserSnapshotEntity u SET u.username = :newUsername WHERE u.username = :oldUsername")
    void changeUsernameInAllSnapshot(String oldUsername, String newUsername);

}
