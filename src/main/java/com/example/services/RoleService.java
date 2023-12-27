package com.example.services;

import com.example.entity.RoleEntity;
import com.example.mapper.RoleMapper;
import com.example.repositories.RoleEntityRepository;
import com.example.users.UserRole;
import com.example.exceptions.*;
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

}
