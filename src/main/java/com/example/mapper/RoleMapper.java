package com.example.mapper;

import com.example.entity.RoleEntity;
import com.example.users.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);


    @Mapping(source = "name", target = "name")
    @Mapping(source = "path", target = "homeDir")
    @Mapping(source = "isAdmin", target = "isAdmin")
    @Mapping(source = "isEnabled", target = "isEnabled")
    @Mapping(source = "canWrite", target = "canWrite")
    @Mapping(source = "uploadSpeed", target = "uploadSpeed")
    @Mapping(source = "downloadSpeed", target = "downloadSpeed")
    UserRole roleEntityToUserRole(RoleEntity roleEntity);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "homeDir", target = "path")
    @Mapping(source = "isAdmin", target = "isAdmin")
    @Mapping(source = "isEnabled", target = "isEnabled")
    @Mapping(source = "canWrite", target = "canWrite")
    @Mapping(source = "uploadSpeed", target = "uploadSpeed")
    @Mapping(source = "downloadSpeed", target = "downloadSpeed")
    RoleEntity userRoleToRoleEntity(UserRole userRole);
}
