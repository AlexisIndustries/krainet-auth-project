package com.alexisindustries.krainet.auth.mapper;

import com.alexisindustries.krainet.auth.kafka.message.CreatedUserMessage;
import com.alexisindustries.krainet.auth.kafka.message.UserMessage;
import com.alexisindustries.krainet.auth.model.User;
import com.alexisindustries.krainet.auth.model.dto.CreateUserDto;
import com.alexisindustries.krainet.auth.model.dto.UserDto;
import com.alexisindustries.krainet.auth.model.dto.UserMeDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);
    UserDto toUserDto(User user);
    User toEntity(CreateUserDto createUserDto);
    CreateUserDto toCreateUserDto(User user);
    User toEntity(UserMeDto userMeDto);
    UserMeDto toUserMeDto(User user);
    UserMessage toUserNotificationDTO(User user);
    CreatedUserMessage toCreatedUserNotificationDTOFromCreateDTO(CreateUserDto createUserDto);
}