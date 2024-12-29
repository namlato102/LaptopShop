package com.namlato.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.namlato.laptopshop.service.UploadService;
import com.namlato.laptopshop.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.namlato.laptopshop.domain.User;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
import jakarta.validation.Valid;

@Controller
public class UserController {

    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UploadService uploadService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/")
    public String getHomePage() {
        List<User> arrUsers = this.userService.getAllUsersByEmail("example01@gmail.com");
        System.out.println(arrUsers);
        return "index";
    }

    //create a new user
    //displaying data in the form, and create a model attribute "newUser" to bind the form data to the User object.
    @GetMapping("/admin/user/create") // GET
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    //When the form is submitted, the POST handler is invoked and the form is automatically bound to the "newUser" argument that passed in modelAttribute.
    @PostMapping(value = "/admin/user/create")
    public String createdUserPage(@ModelAttribute("newUser") @Valid User createdUser,
                                  BindingResult newUserBindingResult,
                                  @RequestParam("uploadFile") MultipartFile file) {
        //System.out.println("New user from controller: " + createdUser);
        //List<FieldError> errors = newUserBindingResult.getFieldErrors();
        //for (FieldError error : errors) {
        //    System.out.println(">>>>" + error.getField() + " - " + error.getDefaultMessage());
        //}

        // validate
        if (newUserBindingResult.hasErrors()) {
            return "admin/user/create";
        }

        // handle upload file
        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        //encode password
        String hashPassword = this.passwordEncoder.encode(createdUser.getPassword());
        createdUser.setAvatar(avatar);
        createdUser.setPassword(hashPassword);
        createdUser.setRole(this.userService.getRoleByName(createdUser.getRole().getName()));
        // save
        this.userService.handleSaveUser(createdUser);
        return "redirect:/admin/user";
    }

    //display all users
    //using model attribute "users" to bind the data to the view.
    @RequestMapping("/admin/user")
    public String getUserPage(Model model, @RequestParam("page") Optional<String> pageOptional) {
        // pass page parameter to url, default page = 1
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                // convert from String to int
                page = Integer.parseInt(pageOptional.get());
            }
        } catch (Exception e) {
            // page = 1
            // TODO: handle exception
        }
        Pageable pageable = PageRequest.of(page - 1, 1);
        Page<User> usersPage = this.userService.getAllUsers(pageable);
        List<User> users = usersPage.getContent();
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
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
        //System.out.println("current user: " + currentUser);
        User updatedUser = this.userService.getUserById(currentUser.getId());
        //System.out.println("Updated user: " + updatedUser);
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
