package wooribe.zarit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // '/images/**' URL 패턴으로 요청이 오면
        // 'file:///C:/project_images/' 경로에서 파일을 찾아 제공합니다.
        // 중요: 이 경로는 예시이며, 실제 이미지를 저장할 폴더 경로로 수정해야 합니다.
        // 또한, 애플리케이션을 실행하기 전에 해당 폴더를 직접 생성해야 합니다.
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:project_images/");
    }
}
