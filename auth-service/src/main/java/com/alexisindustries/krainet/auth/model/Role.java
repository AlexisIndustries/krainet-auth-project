package com.alexisindustries.krainet.auth.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author <a href="https://github.com/AlexisIndustries">AlexisIndustries</a>
 */
public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
