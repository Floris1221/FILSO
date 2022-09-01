package com.ale.filso.seciurity;

import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";//""/" + BakeryConst.PAGE_STOREFRONT;


    public static final String LOGOUT_URL = "/";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//-- oryginal
//        super.configure(http);
//        setLoginView(http, LoginView.class, LOGOUT_URL);

        // Not using Spring CSRF here to be able to use plain HTML for the login page
        http.csrf().disable() // (1)

                // Register our CustomRequestCache that saves unauthorized access attempts, so
                // the user is redirected after login.
                .requestCache().requestCache(new CustomRequestCache()) // (2)

                // Restrict access to our application.
                .and().authorizeRequests()

                // Allow all flow internal requests.
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll() // (3)

                // Allow all requests by logged in users.
                .anyRequest().authenticated() // (4)

                // Configure the login page.
                .and().formLogin().loginPage(LOGIN_URL).permitAll() // (5)
                .loginProcessingUrl(LOGIN_PROCESSING_URL) // (6)
                .failureUrl(LOGIN_FAILURE_URL)

                // Configure logout
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        // oryginal
        // web.ignoring().antMatchers("/images/*.png");
        web.ignoring().antMatchers(
                //password change request
                //"/passwordChange/**",

                //password forgot request
                //"/passwordForgot/**",

                //PasswordRequestInvalid
                //"/passwordRequestInvalid/**",

                // rest api
                "/api/**",

                // client-side JS code
                "/VAADIN/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",

                // icons and images
                "/icons/**",
                "/images/**",

                // (development mode) H2 debugging console
                "/h2-console/**"
        );
    }

}
