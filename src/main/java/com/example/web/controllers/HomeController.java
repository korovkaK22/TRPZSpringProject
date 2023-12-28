package com.example.web.controllers;

import com.example.services.HomeService;
import com.example.services.UserService;
import com.example.users.ServerUser;
import com.example.users.UserRole;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.LinkedList;
import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController {
    private static final Logger logger = LogManager.getRootLogger();
    private UserService userService;
    private HomeService homeService;


    @GetMapping("/")
    private String enterPage() {

        return "/WEB-INF/jsp/loginPage.jsp";
    }

    @PostMapping("/")
    private String loginPage(Model model,
                             HttpSession session,
                             @RequestParam @NotBlank String username,
                             @RequestParam @NotBlank String password) {
        //Перевірка на користувача
        if (!userService.checkPasswords(username, password)) {
           logger.warn("Fail login: " + username);
           model.addAttribute("failMessage", "Неправильний логін або пароль");
            return "/WEB-INF/jsp/loginPage.jsp";
        }


        //Перевірка на адміна
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        ServerUser user = userService.getServerUserByName(username).get();

        if (!user.getRole().getIsAdmin()) {
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
            return "redirect:/";
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
        model.addAttribute("globalDownloadSpeed", UserRole.getGlobalDownloadSpeed());
        model.addAttribute("globalUploadSpeed", UserRole.getGlobalUploadSpeed());
        return "/WEB-INF/jsp/homePage.jsp";
    }


    @PostMapping("/global-speed")
    public String setGlobalSpeed(HttpSession session,
                                  @RequestParam int downloadSpeed,
                                  @RequestParam int uploadSpeed) {
        if (session.getAttribute("user") == null) {
            return "redirect:/";
        }
        logger.info(String.format("Changed max users speed: download: %d, upload: %d (b/s) ", downloadSpeed, uploadSpeed));
        UserRole.setGlobalDownloadSpeed(downloadSpeed);
        UserRole.setGlobalUploadSpeed(uploadSpeed);
        return "redirect:/home";
    }

}
