package bakery.caker.config;

import bakery.caker.service.OAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final OAuthUserService oAuthUserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .anyRequest().permitAll() //일단은 모두 허용
                .and()
                .logout()
                .logoutSuccessUrl("/main")
                .and()
                .oauth2Login()
                .defaultSuccessUrl("/main")
                .userInfoEndpoint()
                .userService(oAuthUserService);

        return http.build();
    }
}
