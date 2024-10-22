package com.namlato.laptopshop.controller.client;

import java.util.List;

import com.namlato.laptopshop.domain.dto.RegisterDTO;
import com.namlato.laptopshop.service.ProductService;
import com.namlato.laptopshop.domain.Product;

import com.namlato.laptopshop.service.UserService;
import com.namlato.laptopshop.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomePageController {

    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public HomePageController(ProductService productService, UserService userService, PasswordEncoder passwordEncoder) {
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> products = this.productService.fetchProducts();
        model.addAttribute("products", products);
        return "client/homepage/show";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("registerUser") RegisterDTO registerDTO) {
        // convert RegisterDTO to User
        User registeredUser = this.userService.registerDTOtoUser(registerDTO);
        String hashPassword = this.passwordEncoder.encode(registeredUser.getPassword());

        registeredUser.setPassword(hashPassword);
        registeredUser.setRole(this.userService.getRoleByName("USER"));
        // save
        this.userService.handleSaveUser(registeredUser);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "client/auth/login";
    }
}
