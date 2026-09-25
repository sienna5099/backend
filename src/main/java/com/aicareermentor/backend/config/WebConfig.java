package com.aicareermentor.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(
                        "/api/profile/**",
                        "/api/assessment/**",
                        "/api/assessment-results/**",
                        "/api/careers/recommendations/**",
                        "/api/mentors/**",
                        "/api/mentor-selection/**",
                        "/api/mentor-chat/**",
                        "/api/skill-gap/**",
                        "/api/skill-assessment/**",
                        "/api/roadmap/**");
    }
}
