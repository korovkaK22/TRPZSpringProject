package com.example.web.controllers;

import com.example.security.PasswordEncryptorImpl;
import com.example.server.FTPServer;
import com.example.services.HomeService;
import com.example.services.UserService;
import com.example.users.ServerUser;
import com.example.users.states.AdminUserState;
import com.example.users.states.CustomUserStateBuilder;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(FTPServer.class);
    private UserService userService;
    private HomeService homeService;


    @GetMapping("/")
    private String enterPage() {

        return "/WEB-INF/jsp/loginPage.jsp";
    }

    @PostMapping("/")
    private String loginPage( Model model,
                              HttpSession session,
                              @RequestParam @NotBlank String username,
                              @RequestParam @NotBlank String password) {
        //Перевірка на користувача
        try {
            if (!userService.checkPasswords(username, password)) {
                model.addAttribute("failMessage", "Неправильний логін або пароль");
            }
        } catch (Exception e) {
            logger.warn("Fail login: "+ e.getMessage());
            model.addAttribute("failMessage", "Неправильний логін або пароль");
        }
        if (model.containsAttribute("failMessage")) {
            return "/WEB-INF/jsp/loginPage.jsp";
        }

        //Перевірка на адміна
        ServerUser user = userService.getServerUserByName(username);
        if (!user.getState().getIsAdmin()){
            model.addAttribute("failMessage", "Даний аккаунт не є адміном!");
            return "/WEB-INF/jsp/loginPage.jsp";
        }

        //Все пройшло добре
        session.setAttribute("user", user);
        return "redirect:/home?page=1";
    }


    @GetMapping("/404")
    private String notFoundPage() {

        return "/WEB-INF/jsp/notFoundPage.jsp";
    }

    @GetMapping("/home")
    public String enterPage(Model model, HttpSession session,
                            @RequestParam(defaultValue = "1") @Positive int page) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "/WEB-INF/jsp/homePage.jsp";
        }
        final int pageSize = 8;

        List<ServerUser> allUsers = new LinkedList<>(userService.getAllUsers());

        // for (int i =0; i < 20; i++){
        // allUsers.add(allUsers.get(1));}

        int startIndex = (page - 1) * pageSize;
        if (startIndex > allUsers.size()) {
            return "redirect:/404";
        }
        int endIndex = Math.min(startIndex + pageSize, allUsers.size());
        boolean hasNextPage = endIndex < allUsers.size();
        model.addAttribute("user", user);
        model.addAttribute("pageNumber", page);
        model.addAttribute("hasNextPage", hasNextPage);
        model.addAttribute("users", allUsers.subList(startIndex, endIndex));
        model.addAttribute("info", homeService.getInformation());
        return "/WEB-INF/jsp/homePage.jsp";
    }

}
