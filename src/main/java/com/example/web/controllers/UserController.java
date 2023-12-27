package com.example.web.controllers;

import com.example.server.FTPServer;
import com.example.services.RoleService;
import com.example.services.UserService;
import com.example.users.ServerUser;
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

import java.util.Optional;


@Controller
@Transactional
@AllArgsConstructor
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(FTPServer.class);
    private UserService userService;
   // private UserSnapshotService userSnapshotService;
    private RoleService roleService;



    @PostMapping("/users/create-user")
    public String createUserDefault(Model model, HttpSession session,
                                    @RequestParam("username") @NotBlank String username,
                                    @RequestParam("password") @NotBlank String password,
                                    @RequestParam("myDropdown") @NotBlank int roleId) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "redirect:/";
        }

        model.addAttribute("roles", roleService.getAllRoles());

        int result = userService.createUser(username, password, roleId);
        if (result ==-1){
            model.addAttribute("failMessage", "Помилка при створенні користувача");
            return "/WEB-INF/jsp/createUserPage.jsp";
        }
        return "redirect:/users/" + result;
    }

//    @PostMapping("/users/create-user-custom")
//    public String createUserWithCustomRole(Model model, HttpSession session,
//                                           @RequestParam("username") String username,
//                                           @RequestParam("password") String password,
//                                           @RequestParam("stateUsername") @NotBlank String stateUsername,
//                                           @RequestParam("stateDirectory") @NotBlank String stateDirectory,
//                                           @RequestParam(name ="stateCanWrite", defaultValue = "false")  boolean canWrite,
//                                           @RequestParam(name ="stateIsAdmin", defaultValue = "false")  boolean isAdmin,
//                                           @RequestParam(name ="stateIsEnabled", defaultValue = "true")  boolean isEnabled,
//                                           @RequestParam("customDownloadSpeed") @NotBlank @Positive int downloadSpeed,
//                                           @RequestParam("customUploadSpeed") @NotBlank @Positive int uploadSpeed) {
//        ServerUser user;
//        //Не зареєстрований
//        if ((user = (ServerUser) session.getAttribute("user")) == null) {
//            return "/WEB-INF/jsp/createUserPage.jsp";
//        }
      //  List<UserRole> allDefaultStates = roleService.getAllDefaultStates();
      //  model.addAttribute("allDefaultStates", allDefaultStates);

        //Чи є вже такий користувач
//        if (userService.doesUserExist(username)) {
//            model.addAttribute("failMessage", "Користувач з таким іменем вже існує!");
//            return "/WEB-INF/jsp/createUserPage.jsp";
//        }
//        if (roleService.doesStateExist(stateUsername)) {
//            model.addAttribute("failMessage", "Цей статус вже існує!");
//            return "/WEB-INF/jsp/createUserPage.jsp";
//        }
//
//        userService.createUser(username, password, stateUsername);
//        roleService.createState(stateUsername,
//                stateDirectory,
//                canWrite,
//                isAdmin,
//                isEnabled,
//                downloadSpeed,
//                uploadSpeed);

//        return "redirect:/users/" + username;
//    }



    @PostMapping("/users/delete-user")
    public String createUserDefault(Model model, HttpSession session,
                                    @RequestParam @NotBlank String username) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "redirect:/home";
        }
        int id = userService.getUserId(username);
        if (id==-1){
            return "redirect:/home";
        }
        userService.deleteUser(id);
        return "redirect:/home";
    }


    @GetMapping("/createUser")
    public String createUserPage(Model model, HttpSession session) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "/WEB-INF/jsp/createUserPage.jsp";
        }

        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());

        return "/WEB-INF/jsp/createUserPage.jsp";
    }

    @GetMapping("/users/{id}")
    public String viewUserPage(Model model, HttpSession session,
                               @PathVariable int id) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "redirect:/";
        }

        //Знайти користувача
        Optional<ServerUser> serverUserOpt =userService.getServerUserById(id);
        if (serverUserOpt.isEmpty()){
            return "redirect:/404";
        }

        ServerUser serverUser = serverUserOpt.get();


        model.addAttribute("serverUser", serverUser);
        model.addAttribute("roles", roleService.getAllRoles());
        // model.addAttribute("mementos", userSnapshotService.getSnapshotsByUser(username));

        return "/WEB-INF/jsp/viewUser.jsp";
    }
    @PostMapping("/users/change-role")
    public String changeUserRole(Model model, HttpSession session,
                                 @RequestParam int id,
                                 @RequestParam("myDropdown") int newRole){

        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "redirect:/";
        }
        //Знайти користувача
        Optional<ServerUser> serverUserOpt =userService.getServerUserById(id);
        if (serverUserOpt.isEmpty()){
            return "redirect:/404";
        }
        userService.changeRole(id, newRole);
        return "redirect:/users/" + serverUserOpt.get().getId();
    }


}
