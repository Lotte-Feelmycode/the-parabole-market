package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    void deleteById(Long userId);

}
