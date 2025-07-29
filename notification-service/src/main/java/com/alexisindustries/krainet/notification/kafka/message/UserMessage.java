package com.alexisindustries.krainet.notification.kafka.message;

import lombok.Data;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Data
public class UserMessage {
    private String username;
    private String email;
}
