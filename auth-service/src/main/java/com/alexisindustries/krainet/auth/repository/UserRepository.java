package com.alexisindustries.krainet.auth.repository;

import com.alexisindustries.krainet.auth.model.Role;
import com.alexisindustries.krainet.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAllByRole(Role role);

    boolean existsUserByUsername(String username);
}
