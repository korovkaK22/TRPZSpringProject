package com.example.web.controllers;

import com.example.services.HomeService;
import com.example.services.RoleService;
import com.example.users.ServerUser;
import com.example.users.UserRole;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class RolesController {
    private final RoleService roleService;

    @GetMapping("/roles")
    private String enterRolesPage(Model model,
                                  HttpSession session) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "redirect:/";
        }
        model.addAttribute("roles", roleService.getAllRoles());
        return "/WEB-INF/jsp/roles/rolesPage.jsp";
    }


    @GetMapping("/roles/{id}")
    public String viewRole(Model model, HttpSession session,
                           @PathVariable int id) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "redirect:/";
        }

        //Знайти роль
        Optional<UserRole> userRoleOptional = roleService.getUserRole(id);
        if (userRoleOptional.isEmpty()) {
            return "redirect:/404";
        }

        UserRole userRole = userRoleOptional.get();


        model.addAttribute("role", userRole);

        return "/WEB-INF/jsp/roles/viewRole.jsp";
    }

    @GetMapping("/roles/{id}/edit")
    public String editRolePage(Model model, HttpSession session,
                           @PathVariable int id) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "redirect:/";
        }

        //Знайти роль
        Optional<UserRole> userRoleOptional = roleService.getUserRole(id);
        if (userRoleOptional.isEmpty()) {
            return "redirect:/404";
        }

        UserRole userRole = userRoleOptional.get();
        model.addAttribute("role", userRole);
        return "/WEB-INF/jsp/roles/editRolePage.jsp";
    }

    @PostMapping("/roles/edit") //=======
    public String editRole(Model model, HttpSession session,
                           @RequestParam int id,
                           @RequestParam("roleUsername") String roleUsername,
                           @RequestParam("roleDirectory") @NotBlank String roleDirectory,
                           @RequestParam(name = "roleCanWrite", defaultValue = "false") boolean canWrite,
                           @RequestParam(name = "roleIsAdmin", defaultValue = "false") boolean isAdmin,
                           @RequestParam(name = "roleIsEnabled", defaultValue = "false") boolean isEnabled,
                           @RequestParam("customDownloadSpeed") @NotBlank @Positive int downloadSpeed,
                           @RequestParam("customUploadSpeed") @NotBlank @Positive int uploadSpeed) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "redirect:/";
        }

        roleService.editRole(id,roleUsername, roleDirectory, canWrite,isAdmin, isEnabled, downloadSpeed, uploadSpeed);
        return "redirect:/roles/"+ id;
    }

    @GetMapping("/roles/create")
    public String getCreatePage(HttpSession session) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "redirect:/";
        }
        return "/WEB-INF/jsp/roles/createRolePage.jsp";
    }

    @PostMapping("/roles/create")
    public String createRole(Model model, HttpSession session,
                             @RequestParam("roleUsername") String roleUsername,
                             @RequestParam("roleDirectory") @NotBlank String roleDirectory,
                             @RequestParam(name = "roleCanWrite", defaultValue = "false") boolean canWrite,
                             @RequestParam(name = "roleIsAdmin", defaultValue = "false") boolean isAdmin,
                             @RequestParam(name = "roleIsEnabled", defaultValue = "false") boolean isEnabled,
                             @RequestParam("customDownloadSpeed") @NotBlank @Positive int downloadSpeed,
                             @RequestParam("customUploadSpeed") @NotBlank @Positive int uploadSpeed) {
        ServerUser user;
        //Не зареєстрований
        if ((user = (ServerUser) session.getAttribute("user")) == null) {
            return "redirect:/";
        }
        int id = roleService.createRole(roleUsername, roleDirectory, canWrite,isAdmin, isEnabled, downloadSpeed, uploadSpeed);
        return "redirect:/roles/"+ id;
    }


}
