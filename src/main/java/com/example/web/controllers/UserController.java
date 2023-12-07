package com.example.web.controllers;

import com.example.entity.UserEntity;
import com.example.server.FTPServer;
import com.example.services.UserService;
import com.example.services.UserSnapshotService;
import com.example.users.ServerUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

@Controller
@AllArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(FTPServer.class);
    private UserService userService;
    private UserSnapshotService userSnapshotService;

    @GetMapping("/home")
    public String enterPage(Model model, HttpSession session,
                            @RequestParam(defaultValue = "1") @Positive int page) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) ==null){
            return "/WEB-INF/jsp/homePage.jsp";
        }
        final int pageSize = 8;

        //=============ЗАТИЧКА=============
        List<ServerUser> allUsers = new LinkedList<>(userService.getAllUsers());
        allUsers.remove(user);

        //=============ЗАТИЧКА=============
        for(int i = 0; i < 17; i++){
        allUsers.add(allUsers.get(0));}

        int startIndex = (page - 1) * pageSize;
        if (startIndex > allUsers.size()){
            return "redirect:/404";
        }
        int endIndex = Math.min(startIndex + pageSize, allUsers.size());
        boolean hasNextPage = endIndex < allUsers.size();
        model.addAttribute("user", user);
        model.addAttribute("pageNumber", page);
        model.addAttribute("hasNextPage", hasNextPage);
        model.addAttribute("users", allUsers.subList(startIndex, endIndex));
        return "/WEB-INF/jsp/homePage.jsp";
    }



    @GetMapping("/users/{username}")
    public String viewUserPage(Model model, HttpSession session,
                               @PathVariable @NotBlank String username) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) ==null){
            return "/WEB-INF/jsp/viewUser.jsp";
        }

        //Знайти користувача
        ServerUser serverUser;
        try {
            serverUser = userService.getServerUserByName(username);
        } catch (Exception e){
            return "redirect:/404";
        }

        model.addAttribute("user", user);
        model.addAttribute("serverUser", serverUser);
        model.addAttribute("mementos",  userSnapshotService.getSnapshotsByUser(username));

        return "/WEB-INF/jsp/viewUser.jsp";
    }

}
