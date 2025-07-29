package com.alexisindustries.krainet.auth.kafka.producer;

import com.alexisindustries.krainet.auth.kafka.message.CreatedUserMessage;
import com.alexisindustries.krainet.auth.kafka.message.UserMessage;
import com.alexisindustries.krainet.auth.mapper.UserMapper;
import com.alexisindustries.krainet.auth.model.Role;
import com.alexisindustries.krainet.auth.model.User;
import com.alexisindustries.krainet.auth.model.dto.CreateUserDto;
import com.alexisindustries.krainet.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, UserMessage> userMessageKafkaTemplate;

    private final KafkaTemplate<String, CreatedUserMessage> createdUserMessageKafkaTemplate;

    private final UserRepository userRepository;

    private final UserMapper mapper;

    @Value("${kafka-topics.created-user}")
    private String createdUserTopic;

    @Value("${kafka-topics.updated-user}")
    private String updatedUserTopic;

    @Value("${kafka-topics.deleted-user}")
    private String deletedUserTopic;

    public void sendCreatedUserNotification(CreateUserDto user) {
        userRepository.findAllByRole(Role.ADMIN).forEach(admin -> createdUserMessageKafkaTemplate
                .send(createdUserTopic, admin.getEmail(), mapper.toCreatedUserNotificationDTOFromCreateDTO(user)));
    }

    public void sendUpdatedUserNotification(User user) {
        sendUserNotification(updatedUserTopic, user);
    }

    public void sendDeletedUserNotification(User user) {
        sendUserNotification(deletedUserTopic, user);
    }

    private void sendUserNotification(String topic, User user) {
        userRepository.findAllByRole(Role.ADMIN).forEach(admin -> userMessageKafkaTemplate
                .send(topic, admin.getEmail(), mapper.toUserNotificationDTO(user)));
    }

}
