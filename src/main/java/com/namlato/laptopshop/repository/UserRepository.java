//https://spring.io/guides/gs/accessing-data-jpa
package com.namlato.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.namlato.laptopshop.domain.User;
import java.util.List;

//crud: create, read, update, delete
public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);
    List<User> findAllByEmail(String email);
    List<User> findAll();
    User findById(long id); // null if not found
    void deleteById(long id);
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
