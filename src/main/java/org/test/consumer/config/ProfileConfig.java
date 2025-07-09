package org.test.consumer.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ProfileConfig {
    
    private final Environment environment;

    public ProfileConfig(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void validateActiveProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        
        if (activeProfiles.length == 0) {
            throw new IllegalStateException("No active profile set. Please set either 'jms' or 'kafka' as active profile. " +
                    "You can set it in application.properties with 'spring.profiles.active=jms' or 'spring.profiles.active=kafka' " +
                    "or using environment variable 'SPRING_PROFILES_ACTIVE'.");
        }
        
        for (String profile : activeProfiles) {
            if ("jms".equals(profile) || "kafka".equals(profile)) {
                return;
            }
        }
        
        throw new IllegalStateException("Invalid active profile. Please use either 'jms' or 'kafka' profile. " +
                "Current active profiles: " + String.join(", ", activeProfiles));
    }
}
