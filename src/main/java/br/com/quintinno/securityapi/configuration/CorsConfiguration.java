package br.com.quintinno.securityapi.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Value("${api.securityapi.url.origins}")
    private String url;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
         corsRegistry.addMapping("/**")
                .allowedOrigins(this.url)
                .allowedMethods("GET", "POST", "DELETE", "PUT");
    }

}
