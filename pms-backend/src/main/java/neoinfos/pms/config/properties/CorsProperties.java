package neoinfos.pms.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * packageName    : neoinfos.pms.config.properties
 * fileName       : CorsProperties
 * author         : JAEIK
 * date           : 8/4/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 8/4/26        JAEIK       최초 생성
 */
@ConfigurationProperties("pms.cors")
public record CorsProperties(
        Boolean allowCredentials,
        String[] allowedHeaders,
        String[] allowedOrigins,
        String[] allowedMethods,
        String[] exposeHeaders,
        Long maxAge
) {
}
