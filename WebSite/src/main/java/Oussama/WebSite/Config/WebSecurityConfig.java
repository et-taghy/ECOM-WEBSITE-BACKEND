package Oussama.WebSite.Config;

import Oussama.WebSite.Filters.JwtAuthenticationEntryPoint;
import Oussama.WebSite.Filters.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Autowired
    private JwtAuthenticationEntryPoint point;
@Autowired
private final JwtRequestFilter jwtRequestFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(auth-> {
                            try {
                                auth.requestMatchers(mvcMatcherBuilder.pattern("/auth/Login")).permitAll()
                                        .requestMatchers(mvcMatcherBuilder.pattern("/auth/Sign-Up")).permitAll()
                                        .requestMatchers(mvcMatcherBuilder.pattern("/api/**"))
                                        .authenticated()
                                        .anyRequest()
                                        .authenticated()
                                        .and().exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
        ;
        return http.build();
    }
@Bean
    public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
}
@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{
    return config.getAuthenticationManager();
}
}
