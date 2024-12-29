package com.namlato.laptopshop.service;

import java.util.List;

import com.namlato.laptopshop.domain.Role;
import com.namlato.laptopshop.domain.User;
import com.namlato.laptopshop.repository.OrderRepository;
import com.namlato.laptopshop.repository.ProductRepository;
import com.namlato.laptopshop.repository.RoleRepository;
import org.springframework.stereotype.Service;
import com.namlato.laptopshop.repository.UserRepository;
import com.namlato.laptopshop.domain.dto.RegisterDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       ProductRepository productRepository,
                       OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public void handleSaveUser(User user) {
        this.userRepository.save(user);
    }

    public Page<User> getAllUsers(Pageable page) {
        return this.userRepository.findAll(page);
    }

    public List<User> getAllUsersByEmail(String email) {
        return this.userRepository.findAllByEmail(email);
    }

    public User getUserById(long id) {
        return this.userRepository.findById(id);
    }

    public void deleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public Role getRoleByName(String name) {
        return this.roleRepository.findByName(name);
    }

    // act as Mapper to convert RegisterDTO to User
    public User registerDTOtoUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setFullName(registerDTO.getFirstName() + " " + registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        return user;
    }

    public boolean checkEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public long countUsers() {
        return this.userRepository.count();
    }
    public long countProducts() {
        return this.productRepository.count();
    }
    public long countOrders() {
        return this.orderRepository.count();
    }
}
