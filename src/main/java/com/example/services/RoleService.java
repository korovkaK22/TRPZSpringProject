package com.example.services;

import com.example.entity.RoleEntity;
import com.example.repositories.RoleEntityRepository;
import com.example.users.UserRole;
import com.example.exceptions.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
@AllArgsConstructor
public class RoleService {
    private ApplicationContext applicationContext;
    private RoleEntityRepository roleEntityRepository;



}
