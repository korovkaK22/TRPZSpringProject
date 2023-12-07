package com.example.visitor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ServerInformation {
    private int currentUsersNumber;
    private int maxUsersNumber;
    private List<String> allUsersName;
    private List<String> allOnlineUsersName;

}
