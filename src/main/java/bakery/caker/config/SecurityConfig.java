package bakery.caker.config;

import bakery.caker.service.OAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/main").permitAll()

                .antMatchers(HttpMethod.POST, "/orders").hasRole(Authority.CLIENT.name())
                .antMatchers("/orders/myOrder").hasRole(Authority.CLIENT.name())

                .antMatchers("/orders/myPin").hasRole(Authority.BAKER.name())
                .antMatchers("/store/{store_id}/events/**").hasRole(Authority.BAKER.name())
                .antMatchers("**/events/**").hasRole(Authority.BAKER.name())

                .antMatchers("/stores/myStore").hasAnyRole(Authority.BAKER.name(), Authority.TRAINEE.name())
                .antMatchers(HttpMethod.POST, "/orders/{order_id}/comments/**").hasAnyRole(Authority.BAKER.name(), Authority.CLIENT.name())

                .anyRequest().authenticated() //일단은 모두 허용
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
