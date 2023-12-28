package com.example.repositories;

import com.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findUserEntityByUsernameIgnoreCase(String username);

    Optional<UserEntity> findUserEntityById(Integer id);

}
