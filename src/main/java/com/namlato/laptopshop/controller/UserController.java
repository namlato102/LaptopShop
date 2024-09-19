package com.namlato.laptopshop.controller;

import com.namlato.laptopshop.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.namlato.laptopshop.domain.User;

@Controller
public class UserController {

    final private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        String test = this.userService.handleHello();
        model.addAttribute("test", test);
        model.addAttribute("test2", "Hello from UserController");
        return "index";
    }

    //displaying data in the form, and create a model attribute
    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    //When the form is submitted, the POST handler is invoked and the form is automatically bound to the "newUser" argument that passed in modelAttribute.
    @RequestMapping(value = "/admin/user/created", method = RequestMethod.POST)
    public String createdUserPage(Model model, @ModelAttribute("newUser") User createdUser) {
        System.out.println(" run here " + createdUser);
        model.addAttribute("id", createdUser.getId());
        model.addAttribute("name", createdUser.getFullName());
        model.addAttribute("phone", createdUser.getPhone());
        return "index";
    }
}
