package com.alexisindustries.krainet.auth.kafka.message;

import lombok.Data;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
@Data
public class CreatedUserMessage {
    private String username;
    private String email;
    private String password;
}
