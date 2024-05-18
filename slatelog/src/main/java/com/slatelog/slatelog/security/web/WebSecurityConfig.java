package com.slatelog.slatelog.security.web;

// Inversion of Control (IoC)
// --------------------------------------------------------------------------------------------
// IoC design principle in which the control of objects is transferred to the framework.
// The framework manages the creation of objects, the objects' lifecycle, and object dependencies.
// IoC uses DI (Dependency Injection) to implement IoC.
// IoC uses Live Cycle Events to give the developer more control over the application.

// Dependency Injection (DI)
// --------------------------------------------------------------------------------------------
// DI is a mechanism to implement IoC.
// The container injects the dependency into the object.
// (e.g. PasswordEncoder -> PasswordService)
// _Constructor Injection_ is the preferred way that a bean is injected into another bean.
// _Setter Injection_ is another way that a bean is injected into another bean.

// Dependency Tree
// --------------------------------------------------------------------------------------------
// Spring constructs a dependency tree of all beans.
// The dependency tree is used to determine the order in which beans are created.
//    Password Encoder
//           ↓
//    Password Service
//           ↓
//  User Registration Service

// POJO vs Bean
// --------------------------------------------------------------------------------------------
// A Plain Old Java Object (POJO) is an ordinary Java object
// A Bean is a Spring managed object (instance of a class).

// What is a Bean?
// --------------------------------------------------------------------------------------------
// A Bean is a Spring managed object (instance of a class).
// Spring manages the lifecycle of a bean. From creation to destruction.
// Spring can inject a Bean into another Bean (-> Dependency Injection).
// e.g. PasswordService is a Bean (@Service), and we can inject the PasswordEncoder (@Bean) into it.
// There are usually two ways to create a Bean:
// 1. Annotate a class with @Component, @Service, @Repository, @Controller, @RestController, etc.
// 2. Annotate a method with @Bean in a @Configuration class and return an object from that method.

// Annotations used?
// --------------------------------------------------------------------------------------------
// @Configuration to tell Spring that this is a configuration class which contains beans (@Bean)
// @Bean to tell Spring that this method returns a bean

// AAA-Triad
// Authentication
// Authorization
// Accounting

// CIA-Triad
// Confidentiality
// Integrity
// Availability

// Authentication Mechanism
// Basic Authentication
// Cookie Based / Persistence Tokens

// Auth2 / JWT
// Passwordless Authentication (FIDO)
// LDAP
// OpenId

// Credentials

// GrantedAuthority
// "Permission" or "Right"

// Role
// GrantedAuthority
// ROLE_XXX
// "ROLE_ADMIN"
// "ROLE_USER"

// 1. UserDetails (Interface)
//   Represents a principal (user) for Spring Security
//   GrantedAuthorities
//   disabling: email is not verified, admin disables an account
//   locking: number of failed login attempts

// 2. UserDetailsService (Interface)
//   To load user data from a specific source (database) during authentication.
//   Returns a UserDetails to Spring Security

// 3. Security Config
//    Configure SecurityFilterChain

import com.slatelog.slatelog.domain.user.CustomOAuth2User;
import com.slatelog.slatelog.domain.user.Role;
import com.slatelog.slatelog.persistance.UserRepository;
import com.slatelog.slatelog.presentation.views.Views;
import com.slatelog.slatelog.service.CustomOAuth2UserService;
import com.slatelog.slatelog.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {

    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new DaoUserDetailsService(userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // AUTHENTICATION
        http.httpBasic(withDefaults());
        http.formLogin(formLogin -> formLogin.disable());

        // AUTHORIZATION
        http.authorizeHttpRequests(
                        (authorize) ->
                                authorize
                                        .requestMatchers("/api/registration/**", "/api/login/**", "/api/oauth/**", "/oauth/**").permitAll()
                                        .requestMatchers("/api/user/**").hasRole(Role.USER.toString())
                                        .requestMatchers("/api/event/poll").permitAll()
                                        .anyRequest().authenticated()

        );

        // OAuth2 Login
        http.oauth2Login(oauth2Login ->
                oauth2Login
                        .loginPage("/api/user")
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.userService(oAuth2UserService)
                        )
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {
                                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                                CustomOAuth2User customOAuth2User = new CustomOAuth2User(oauth2User);
                                Views.LoginView loginView = userService.processOAuthPostLogin(customOAuth2User.getAttributes().get("email").toString());

                                clearAuthenticationAttributes(request);

                                response.sendRedirect("http://localhost:4200/api/timeline");
                            }

                            protected void clearAuthenticationAttributes(HttpServletRequest request) {
                                var session = request.getSession(false);
                                if (session == null) {
                                    return;
                                }
                                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                            }
                        })
        );


        // CSRF
        http.csrf(csrf -> csrf.disable());

        // CORS
        http.cors(cors -> cors.disable());

        // SESSIONS
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        DefaultSecurityFilterChain filterChain = http.build();
        return filterChain;
    }



}
