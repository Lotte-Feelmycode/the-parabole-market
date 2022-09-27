package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);
    @Query("SELECT u FROM User u WHERE u.password = :password")
    User findByPassword(@Param("password") String password);

}

