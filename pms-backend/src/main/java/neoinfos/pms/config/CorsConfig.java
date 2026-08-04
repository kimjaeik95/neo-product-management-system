package neoinfos.pms.config;

import lombok.RequiredArgsConstructor;
import neoinfos.pms.config.properties.CorsProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * packageName    : neoinfos.pms.config
 * fileName       : CorsConfig
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {
    private final CorsProperties cors;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(cors.allowCredentials())
                .allowedHeaders(cors.allowedHeaders())
                .allowedOrigins(cors.allowedOrigins())
                .allowedMethods(cors.allowedMethods())
                .exposedHeaders(cors.exposeHeaders())
                .maxAge(cors.maxAge());
    }
}
