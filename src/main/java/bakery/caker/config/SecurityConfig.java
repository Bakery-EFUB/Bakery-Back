package bakery.caker.config;

import bakery.caker.service.JwtTokenProvider;
import bakery.caker.service.OAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfig {
    private final OAuthUserService oAuthUserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http

                .csrf().disable()
                .cors().and()
                .headers().frameOptions().disable().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests(authorize -> authorize
                        .requestMatchers(request -> CorsUtils.isPreFlightRequest(request)).permitAll() //preflight 처리
                        .mvcMatchers(HttpMethod.OPTIONS, "/**/*").permitAll() //preflight 처리
                        .mvcMatchers("**/oauth2/**", "/kakaologin", "/main", "/","/css/**","/images/**","/js/**","/profile").permitAll()
                        .mvcMatchers(HttpMethod.GET, "/orders", "/orders/{loc_gu}/{loc_dong}", "/orders/{order_id}", "/orders/newOrder").permitAll()
                        .mvcMatchers(HttpMethod.GET, "/stores", "/stores/recomends", "/stores/search").permitAll()
                        .mvcMatchers(HttpMethod.GET, "/orders/{order_id}/comments").permitAll()

                        .mvcMatchers(HttpMethod.POST, "/orders").hasRole(Authority.CLIENT.name())
                        .mvcMatchers(HttpMethod.PATCH, "/orders").hasRole(Authority.CLIENT.name())
                        .mvcMatchers("/orders/myOrder").hasRole(Authority.CLIENT.name())
                        .mvcMatchers("/orders/myPin").hasRole(Authority.BAKER.name())
                        .mvcMatchers("**/events/**").hasRole(Authority.BAKER.name())
                        .mvcMatchers("/stores/myStore").hasAnyRole(Authority.BAKER.name(), Authority.TRAINEE.name())
                        .mvcMatchers(HttpMethod.POST, "/orders/{order_id}/comments/**").hasAnyRole(Authority.BAKER.name(), Authority.CLIENT.name())
                        .anyRequest().authenticated()
                )


                .logout()
                .and()
                .oauth2Login()
                .successHandler(successHandler)
                .userInfoEndpoint()
                .userService(oAuthUserService);

        return http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class).build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "https://bakery-front-j4r1jvyhh-bakeryshop.vercel.app"));
        configuration.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
