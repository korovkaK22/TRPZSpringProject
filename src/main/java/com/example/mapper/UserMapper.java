package com.example.mapper;

import com.example.entity.UserEntity;
import com.example.users.ServerUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = RoleMapper.class)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "name")
    @Mapping(source = "password", target = "hashedPassword")
    @Mapping(source = "role", target = "role")
    ServerUser userEntityToServerUser(UserEntity userEntity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "username")
    @Mapping(source = "hashedPassword", target = "password")
    @Mapping(target = "role", ignore = true)
    UserEntity serverUserToUserEntity(ServerUser serverUser);
}
