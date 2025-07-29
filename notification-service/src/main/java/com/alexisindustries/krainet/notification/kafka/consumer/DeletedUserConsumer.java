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
public class DeletedUserConsumer {
    private final NotificationService notificationService;

    @KafkaListener(topics = "${kafka-topics.deleted-user}", groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "userMessageContainerFactory")
    public void sendDeletedUserNotification(ConsumerRecord<String, UserMessage> record) {
        UserMessage message = record.value();
        String subject = "Удален пользователь " + message.getUsername();
        String text = String.format(
                "Удален пользователь с именем - %s и почтой - %s",
                message.getUsername(), message.getEmail()
        );
        notificationService.sendNotification(record.key(), subject, text);
    }
}
