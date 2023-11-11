package com.example.dao;

import com.example.users.User;
import com.example.users.UserBuilder;
import com.example.users.states.AdminUserState;
import com.example.users.states.BannedUserState;
import com.example.users.states.DefaultUserState;
import com.example.users.states.PremiumUserState;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@Setter
public class UserRepository {
    @Autowired
    DefaultUserState defaultUserState;
    @Autowired
    AdminUserState adminUserState;
    @Autowired
    BannedUserState bannedUserState;
    @Autowired
    PremiumUserState premiumUserState;


    public List<User> getUsers(){
        List<User> result = new LinkedList<>();
        result.add(new UserBuilder().setName("test").setPassword("test").setState(defaultUserState).build());
        result.add(new UserBuilder().setName("prem").setPassword("prem").setState(premiumUserState).build());
        result.add(new UserBuilder().setName("banned").setPassword("banned").setState(bannedUserState).build());
        return result;
        }


}
