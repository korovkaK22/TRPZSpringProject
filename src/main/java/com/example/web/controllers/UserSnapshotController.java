package com.example.web.controllers;

import com.example.entity.UserSnapshotEntity;
import com.example.services.StatesService;
import com.example.services.UserService;
import com.example.services.UserSnapshotService;
import com.example.users.ServerUser;
import com.example.users.ServerUserSnapshot;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class UserSnapshotController {
    private UserSnapshotService userSnapshotService;
    private UserService userService;
    private StatesService statesService;



    @PostMapping("/create-snapshot")
    private String createNewSnapshot(Model model, HttpSession session,
                                     @RequestParam @NotBlank String username,
                                     @RequestParam @NotBlank String name) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) ==null){
            return "redirect:/users/"+username;
        }

        //Знайти користувача
        ServerUser serverUser;
        try {
            serverUser = userService.getServerUserByName(username);
        } catch (Exception e){
            return "redirect:/404";
        }

        userSnapshotService.saveSnapshot(serverUser, name);

        return "redirect:/users/"+username;
    }

    @GetMapping("/snapshots/{id}")
    public String viewSnapshot(Model model, HttpSession session,
                               @PathVariable @Positive Integer id) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) ==null){
            return "/WEB-INF/jsp/viewSnapshotPage.jsp";
        }

        //Знайти мементо
        UserSnapshotEntity snapshot;
        try {
           snapshot = userSnapshotService.getSnapshotById(id);
        } catch (Exception e){
            return "redirect:/404";
        }

        model.addAttribute("user", user);
        model.addAttribute("memento", snapshot);
        model.addAttribute("state", statesService.getStateByName(snapshot.getStateName()));
        return "/WEB-INF/jsp/viewSnapshotPage.jsp";
    }


    @PostMapping("/snapshots/delete")
    public String deleteSnapshot(Model model, HttpSession session,
                                 @RequestParam @Positive Integer id) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) ==null){
            return "/WEB-INF/jsp/viewSnapshotPage.jsp";
        }

        //Знайти мементо
        UserSnapshotEntity snapshot;
        try {
            snapshot = userSnapshotService.getSnapshotById(id);
        } catch (Exception e){
            return "redirect:/404";
        }
        userSnapshotService.deleteSnapshot(id);

        return "redirect:/users/"+ snapshot.getUsername();
    }


    @PostMapping("/snapshots/set-to-user")
    public String setSnapshot(Model model, HttpSession session,
                                 @RequestParam @Positive Integer id) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) ==null){
            return "/WEB-INF/jsp/viewSnapshotPage.jsp";
        }

        //Знайти мементо
        UserSnapshotEntity snapshot;
        try {
            snapshot = userSnapshotService.getSnapshotById(id);
        } catch (Exception e){
            return "redirect:/404";
        }
        userSnapshotService.setToUserSnapshots(snapshot);

        return "redirect:/users/"+ snapshot.getUsername();
    }

}
