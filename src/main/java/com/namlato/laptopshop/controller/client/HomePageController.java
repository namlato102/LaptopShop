package com.namlato.laptopshop.controller.client;

import java.util.List;

import com.namlato.laptopshop.domain.Order;
import com.namlato.laptopshop.domain.dto.RegisterDTO;
import com.namlato.laptopshop.service.OrderService;
import com.namlato.laptopshop.service.ProductService;
import com.namlato.laptopshop.domain.Product;

import com.namlato.laptopshop.service.UserService;
import com.namlato.laptopshop.domain.User;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Controller
public class HomePageController {

    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OrderService orderService;

    public HomePageController(ProductService productService,
                              UserService userService,
                              PasswordEncoder passwordEncoder,
                              OrderService orderService) {
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String getHomePage(Model model) {
        // List<Product> products = this.productService.fetchProducts();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> prs = this.productService.fetchProducts(pageable);
        List<Product> products = prs.getContent();
        model.addAttribute("products", products);
        return "client/homepage/show";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerUser", new RegisterDTO());
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("registerUser") @Valid RegisterDTO registerDTO,
                                 BindingResult bindingResult) {
        // validate
        if (bindingResult.hasErrors()) {
            return "client/auth/register";
        }

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

    @GetMapping("/access-deny")
    public String getDenyPage(Model model) {

        return "client/auth/deny";
    }

    @GetMapping("/order-history")
    public String getOrderHistoryPage(Model model, HttpServletRequest request) {
        User currentUser = new User();// null
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);
        List<Order> orders = this.orderService.fetchOrderByUser(currentUser);
        model.addAttribute("orders", orders);
        return "client/cart/order-history";
    }
}
