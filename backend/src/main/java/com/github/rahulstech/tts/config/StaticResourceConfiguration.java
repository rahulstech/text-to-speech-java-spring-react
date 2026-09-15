package com.github.rahulstech.tts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("local")
public class StaticResourceConfiguration implements WebMvcConfigurer {

    @Value("${app.static-content.generated-audio}")
    public String generated_audio_location;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/generated-audio/**")
                .addResourceLocations(generated_audio_location);
    }
}
