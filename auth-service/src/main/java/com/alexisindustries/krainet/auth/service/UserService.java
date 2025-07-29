package com.alexisindustries.krainet.auth.service;

import com.alexisindustries.krainet.auth.model.User;
import com.alexisindustries.krainet.auth.model.dto.CreateUserDto;
import com.alexisindustries.krainet.auth.model.dto.UpdateUserDto;
import com.alexisindustries.krainet.auth.model.dto.UserDto;
import com.alexisindustries.krainet.auth.model.dto.UserMeDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
public interface UserService extends UserDetailsService {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    void create(CreateUserDto createUserDto);
    void update(UpdateUserDto updateUserDto);
    void update(long id, UpdateUserDto updateUserDTO);
    List<UserDto> findAll(int page, int size);
    UserDto findByUsername(String username);
    UserDto findByEmail(String email);
    void delete();
    void delete(long id);

    UserMeDto me();
}
