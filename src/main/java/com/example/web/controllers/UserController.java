package com.example.web.controllers;

import com.example.entity.UserEntity;
import com.example.server.FTPServer;
import com.example.services.StatesService;
import com.example.services.UserService;
import com.example.services.UserSnapshotService;
import com.example.users.ServerUser;
import com.example.users.states.AbstractUserState;
import com.example.users.states.CustomUserState;
import com.example.users.states.CustomUserStateBuilder;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

@Controller
@Transactional
@AllArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(FTPServer.class);
    private UserService userService;
    private UserSnapshotService userSnapshotService;
    private StatesService statesService;



    @PostMapping("/users/create-user-default")
    public String createUserDefault(Model model, HttpSession session,
                                    @RequestParam("username") @NotBlank String username,
                                    @RequestParam("password") @NotBlank String password,
                                    @RequestParam("myDropdown") @NotBlank String roleName) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "/WEB-INF/jsp/createUserPage.jsp";
        }
        List<AbstractUserState> allDefaultStates = statesService.getAllDefaultStates();
        model.addAttribute("allDefaultStates", allDefaultStates);

        //Чи є вже такий користувач
        if (userService.doesUserExist(username)) {
            model.addAttribute("failMessage", "Користувач з таким іменем вже існує!");
            return "/WEB-INF/jsp/createUserPage.jsp";
        }
        userService.createUser(username, password, roleName);
        return "redirect:/users/" + username;
    }

    @PostMapping("/users/create-user-custom")
    public String createUserWithCustomRole(Model model, HttpSession session,
                                           @RequestParam("username") String username,
                                           @RequestParam("password") String password,
                                           @RequestParam("stateUsername") @NotBlank String stateUsername,
                                           @RequestParam("stateDirectory") @NotBlank String stateDirectory,
                                           @RequestParam(name ="stateCanWrite", defaultValue = "false")  boolean canWrite,
                                           @RequestParam(name ="stateIsAdmin", defaultValue = "false")  boolean isAdmin,
                                           @RequestParam(name ="stateIsEnabled", defaultValue = "true")  boolean isEnabled,
                                           @RequestParam("customDownloadSpeed") @NotBlank @Positive int downloadSpeed,
                                           @RequestParam("customUploadSpeed") @NotBlank @Positive int uploadSpeed) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "/WEB-INF/jsp/createUserPage.jsp";
        }
        List<AbstractUserState> allDefaultStates = statesService.getAllDefaultStates();
        model.addAttribute("allDefaultStates", allDefaultStates);

        //Чи є вже такий користувач
        if (userService.doesUserExist(username)) {
            model.addAttribute("failMessage", "Користувач з таким іменем вже існує!");
            return "/WEB-INF/jsp/createUserPage.jsp";
        }
        if (statesService.doesStateExist(stateUsername)) {
            model.addAttribute("failMessage", "Цей статус вже існує!");
            return "/WEB-INF/jsp/createUserPage.jsp";
        }

        userService.createUser(username, password, stateUsername);
        statesService.createState(stateUsername,
                stateDirectory,
                canWrite,
                isAdmin,
                isEnabled,
                downloadSpeed,
                uploadSpeed);

        return "redirect:/users/" + username;
    }



    @PostMapping("/users/delete-user")
    public String createUserDefault(Model model, HttpSession session,
                                    @RequestParam @NotBlank String username) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "redirect:/home";
        }
        userService.deleteUser(username);
        return "redirect:/home";
    }

    @GetMapping("/createUser")
    public String createUserPage(Model model, HttpSession session) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "/WEB-INF/jsp/createUserPage.jsp";
        }
        List<AbstractUserState> allDefaultStates = statesService.getAllDefaultStates();

        model.addAttribute("user", user);
        model.addAttribute("allDefaultStates", allDefaultStates);

        return "/WEB-INF/jsp/createUserPage.jsp";
    }


    @GetMapping("/users/{username}")
    public String viewUserPage(Model model, HttpSession session,
                               @PathVariable @NotBlank String username) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "/WEB-INF/jsp/viewUser.jsp";
        }

        //Знайти користувача
        ServerUser serverUser;
        try {
            serverUser = userService.getServerUserByName(username);
        } catch (Exception e) {
            return "redirect:/404";
        }

        model.addAttribute("user", user);
        model.addAttribute("serverUser", serverUser);
        model.addAttribute("mementos", userSnapshotService.getSnapshotsByUser(username));

        return "/WEB-INF/jsp/viewUser.jsp";
    }

}
