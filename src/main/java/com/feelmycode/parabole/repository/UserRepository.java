package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    void deleteById(Long userId);

}
