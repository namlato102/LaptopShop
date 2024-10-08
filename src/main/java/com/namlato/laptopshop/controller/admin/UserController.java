package com.namlato.laptopshop.controller.admin;

import java.util.List;

import com.namlato.laptopshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.namlato.laptopshop.domain.User;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getHomePage() {
        List<User> arrUsers = this.userService.getAllUsersByEmail("example01@gmail.com");
        System.out.println(arrUsers);
        return "index";
    }

    //create a new user
    //displaying data in the form, and create a model attribute "newUser" to bind the form data to the User object.
    @RequestMapping("/admin/user/create") // GET
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    //When the form is submitted, the POST handler is invoked and the form is automatically bound to the "newUser" argument that passed in modelAttribute.
    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)
    public String createdUserPage(@ModelAttribute("newUser") User createdUser) {
        //System.out.println("New user from controller: " + createdUser);
        this.userService.handleSaveUser(createdUser);
        return "redirect:/admin/user";
    }

    //display all users
    //using model attribute "users" to bind the data to the view.
    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/user/users";
    }

    //display detail of a user
    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        //System.out.println("Check user id: " + id);
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/user";
    }

    //update a user
    //using model attribute "currentUser" to bind the current user data to the view.
    @RequestMapping("/admin/user/update/{id}") // GET
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("currentUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String postUpdateUser(@ModelAttribute("currentUser") User currentUser) {
        //System.out.println("After update user: " + currentUser);
        User updatedUser = this.userService.getUserById(currentUser.getId());
        //System.out.println("Before update user: " + updatedUser);
        if (updatedUser != null) {
            updatedUser.setAddress(currentUser.getAddress());
            updatedUser.setFullName(currentUser.getFullName());
            updatedUser.setPhone(currentUser.getPhone());
            this.userService.handleSaveUser(updatedUser);
        }
        return "redirect:/admin/user";
    }

    //delete a user
    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("id", id);
        model.addAttribute("currentUser", currentUser);
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUser(@ModelAttribute("currentUser") User currentUser) {
        this.userService.deleteUser(currentUser.getId());
        return "redirect:/admin/user";
    }
}
