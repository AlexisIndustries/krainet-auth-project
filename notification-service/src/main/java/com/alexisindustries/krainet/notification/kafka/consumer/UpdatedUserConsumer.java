package com.alexisindustries.krainet.notification.kafka.consumer;

import com.alexisindustries.krainet.notification.kafka.message.UserMessage;
import com.alexisindustries.krainet.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Component
@RequiredArgsConstructor
public class UpdatedUserConsumer {
    private final NotificationService notificationService;

    @KafkaListener(topics = "${kafka-topics.updated-user}", groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "userMessageContainerFactory")
    public void sendUpdatedUserNotification(ConsumerRecord<String, UserMessage> record) {
        UserMessage message = record.value();
        String subject = "Изменен пользователь " + message.getUsername();
        String text = String.format(
                "Изменен пользователь с именем - %s и почтой - %s",
                message.getUsername(), message.getEmail()
        );
        notificationService.sendNotification(record.key(), subject, text);
    }
}
