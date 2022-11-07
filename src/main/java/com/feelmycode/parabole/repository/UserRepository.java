package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    void deleteById(Long userId);
    User findByUsername(String username);
    List<User> findAllByUsernameContainsIgnoreCase(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}
