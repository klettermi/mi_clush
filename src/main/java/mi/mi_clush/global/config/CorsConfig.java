package mi.mi_clush.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // 배포 시 cors 적용할 주소 입력
        config.addAllowedHeader("*"); // client 보낸 헤더 중 서버에서 허용할 것
        config.addAllowedMethod("*");
        config.addExposedHeader("*"); // 응답 시 client가 엑세스 할 수 있는 헤더
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config); // OriginPattern의 모든 하위 경로 cors 허용

        return new CorsFilter(source);

    }
}
