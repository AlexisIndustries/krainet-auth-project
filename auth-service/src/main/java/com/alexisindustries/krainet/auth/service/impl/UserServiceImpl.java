package com.alexisindustries.krainet.auth.service.impl;

import com.alexisindustries.krainet.auth.kafka.producer.NotificationProducer;
import com.alexisindustries.krainet.auth.mapper.UserMapper;
import com.alexisindustries.krainet.auth.model.Role;
import com.alexisindustries.krainet.auth.model.User;
import com.alexisindustries.krainet.auth.model.dto.CreateUserDto;
import com.alexisindustries.krainet.auth.model.dto.UpdateUserDto;
import com.alexisindustries.krainet.auth.model.dto.UserDto;
import com.alexisindustries.krainet.auth.model.dto.UserMeDto;
import com.alexisindustries.krainet.auth.repository.UserRepository;
import com.alexisindustries.krainet.auth.service.UserService;
import com.alexisindustries.krainet.auth.util.UserException;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Service
@Log4j2
public class UserServiceImpl implements UserService {

    private final NotificationProducer notificationProducer;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper mapper;

    public UserServiceImpl(NotificationProducer notificationProducer, UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper mapper) {
        this.notificationProducer = notificationProducer;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    @Override
    public boolean existsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }

    @Transactional
    public void create(CreateUserDto createUserDto) {
        User user = mapper.toEntity(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.password()));
        user.setRole(Role.USER);
        userRepository.save(user);
        notificationProducer.sendCreatedUserNotification(createUserDto);
        log.info("Created new user with username - {}", createUserDto.username());
    }

    @Transactional
    public void update(UpdateUserDto updateUserDto) {
        User user = userRepository.findById(getCurrentUser().getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        boolean updated = update(user, updateUserDto);
        if (updated && user.getRole() == Role.USER) {
            notificationProducer.sendUpdatedUserNotification(user);
        }
        log.info("Updated user with username - {}", user.getUsername());
    }

    @Transactional
    public void delete() {
        User user = getCurrentUser();
        userRepository.delete(user);
        if (user.getRole() == Role.USER) {
            notificationProducer.sendDeletedUserNotification(user);
        }
        log.info("Deleted user with username - {}", user.getUsername());
    }

    public List<UserDto> findAll(int page, int size) {
        List<User> users = userRepository.findAll(
                PageRequest.of(page, size, Sort.Direction.DESC, "id")).getContent();
        return users.stream().map(mapper::toUserDto).toList();
    }

    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException("User not found"));
        return mapper.toUserDto(user);
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found"));
        return mapper.toUserDto(user);
    }

    @Transactional
    public void update(long id, UpdateUserDto updateUserDTO) {
        User user = findById(id);
        boolean updated = update(user, updateUserDTO);
        if (updated && user.getRole() == Role.USER) {
            notificationProducer.sendUpdatedUserNotification(user);
        }
        log.info("Updated user by admin with username - {}", user.getUsername());
    }

    @Transactional
    public void delete(long id) {
        User user = findById(id);
        userRepository.delete(user);
        if (user.getRole() == Role.USER) {
            notificationProducer.sendDeletedUserNotification(user);
        }
        log.info("Deleted user by admin with username - {}", user.getUsername());
    }

    private User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found"));
    }

    @Override
    public UserMeDto me() {
        return mapper.toUserMeDto(getCurrentUser());
    }

    boolean update(User user, UpdateUserDto updateUserDto) {
        boolean updated = false;
        if (updateUserDto.username() != null) {
            updated = true;
            user.setUsername(updateUserDto.username());
        }
        if (updateUserDto.email() != null) {
            updated = true;
            user.setEmail(updateUserDto.email());
        }
        if (updateUserDto.firstName() != null) {
            updated = true;
            user.setFirstName(updateUserDto.firstName());
        }
        if (updateUserDto.lastName() != null) {
            updated = true;
            user.setLastName(updateUserDto.lastName());
        }
        if (updateUserDto.password() != null) {
            updated = true;
            user.setPassword(passwordEncoder.encode(updateUserDto.password()));
        }
        return updated;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
