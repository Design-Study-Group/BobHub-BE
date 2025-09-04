package com.bobhub._core.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins("http://localhost:5173", "https://bobhub.vercel.app") // 허용할 출처 목록
        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(true); // 인증 정보(쿠키, Authorization 헤더 등)를 포함한 요청 허용
  }

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    for (HttpMessageConverter<?> converter : converters) {
      if (converter instanceof MappingJackson2HttpMessageConverter) {
        MappingJackson2HttpMessageConverter jacksonConverter =
            (MappingJackson2HttpMessageConverter) converter;
        List<MediaType> supportedMediaTypes =
            new ArrayList<>(jacksonConverter.getSupportedMediaTypes());
        supportedMediaTypes.add(new MediaType("application", "javascript"));
        jacksonConverter.setSupportedMediaTypes(supportedMediaTypes);
      }
    }
  }
}
