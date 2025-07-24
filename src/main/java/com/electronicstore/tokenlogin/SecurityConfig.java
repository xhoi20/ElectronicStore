package com.electronicstore.tokenlogin;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers("/login", "/api/auth/**").permitAll()
                        .requestMatchers("/invoices",  "/invoices/view/**").hasAnyAuthority("ROLE_MANAGER", "ROLE_CASHIER")

                        .requestMatchers( "/invoices/add").hasAnyAuthority( "ROLE_CASHIER")
                        .requestMatchers("/users/user-list", "/suppliers", "/sectors", "/purchases",
                                "/purchase-items", "/items", "/invoices", "/categories").authenticated()

                        .requestMatchers("/users/edit/**", "/users/delete/**",
                                "/suppliers/edit/**", "/suppliers/delete/**",
                                "/sectors/edit/**", "/sectors/delete/**",
                                "/purchases/edit/**", "/purchases/delete/**",
                                "/purchase-items/edit/**", "/purchase-items/delete/**",
                                "/items/edit/**", "/items/delete/**", "/items/restock/**","/invoices/by-cashier", "/invoices/metrics",
                                "/invoices/delete/**", "/categories/edit/**", "/categories/delete/**")
                        .hasAnyAuthority("ROLE_MANAGER")

                        .requestMatchers("/users/user-form", "/suppliers/add", "/sectors/add",
                                "/purchases/add", "/purchase-items/add", "/items/add",
                                "/invoices/add", "/categories/add")
                        .hasAnyAuthority("ROLE_MANAGER")

                        .anyRequest().authenticated()
                )
//                       .requestMatchers("/api/auth/**","/users/**","/login/**","/sectors/**").permitAll()
//                        .requestMatchers("/api/sectors/**").authenticated()

                        //.anyRequest().permitAll())//authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


