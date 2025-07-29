package com.alexisindustries.krainet.notification.kafka.consumer;

import com.alexisindustries.krainet.notification.kafka.message.CreatedUserMessage;
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
public class CreatedUserConsumer {
    private final NotificationService notificationService;

    @KafkaListener(topics = "${kafka-topics.created-user}", groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "createdUserMessageContainerFactory")
    public void sendCreatedUserNotification(ConsumerRecord<String, CreatedUserMessage> record) {
        CreatedUserMessage message = record.value();
        String subject = "Создан пользователь " + message.getUsername();
        String text = String.format(
                "Создан пользователь с именем - %s, паролем - %s и почтой - %s",
                message.getUsername(), message.getPassword(), message.getEmail()
        );
        notificationService.sendNotification(record.key(), subject, text);
    }
}
