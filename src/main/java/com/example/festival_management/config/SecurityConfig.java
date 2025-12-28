package com.example.festival_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.festival_management.entity.User;
import com.example.festival_management.repository.UserRepository;
import com.example.festival_management.security.CustomUserDetailsService;
import com.example.festival_management.security.JwtFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
    private final JwtFilter jwtFilter;
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }
    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console()).disable())
      .headers(h -> h.frameOptions(f -> f.sameOrigin()))
                  //  me JWT theloume stateless session

      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
          // Public endpoints
          .requestMatchers(toH2Console()).permitAll()
          .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
          .requestMatchers("/", "/index.html",
                           "/login.html","/register.html",
                           "/performances.html","/organizer.html","/staff.html",
                           "/favicon.ico","/assets/**","/css/**","/js/**","/festivals.html").permitAll()
          .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/register").permitAll()
          .requestMatchers(HttpMethod.GET,  "/api/festivals/**").permitAll()

          // Protected endpoints requiring authentication
          .requestMatchers("/api/auth/me").authenticated()
          .requestMatchers(HttpMethod.POST, "/api/performances/**").authenticated()
          .requestMatchers(HttpMethod.POST, "/api/festivals/*/performances").authenticated()

          // Allow all other requests; be explicit about security requirements
          .anyRequest().permitAll()
      );

    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

}
