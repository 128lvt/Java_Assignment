package com.example.assignment01.security;

import com.example.assignment01.service.CartInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMcvConfig implements WebMvcConfigurer {
    private CartInterceptor cartInterceptor;

    @Autowired
    public WebMcvConfig(CartInterceptor cartInterceptor) {
        this.cartInterceptor = cartInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cartInterceptor);
    }
}
