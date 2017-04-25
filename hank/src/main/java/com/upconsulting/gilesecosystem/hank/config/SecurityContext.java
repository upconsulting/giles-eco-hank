package com.upconsulting.gilesecosystem.hank.config;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
        // Spring Security ignores request to static resources such as CSS or JS
        // files.
        .ignoring().antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/")
                .loginProcessingUrl("/login/authenticate")
                .failureUrl("/?error=bad_credentials")
                // Configures the logout function
                .and().logout().deleteCookies("JSESSIONID").logoutUrl("/logout")
                .logoutSuccessUrl("/").and().exceptionHandling()
                .accessDeniedPage("/403")
                // Configures url based authorization
                .and()
                .authorizeRequests()
                // Anyone can access the urls
                .antMatchers("/", "/resources/**", "/login/authenticate", "/logout")
                .permitAll()
                // The rest of the our application is protected.
                .antMatchers("/users/**", "/admin/**", "/files/**", "/models/**")
                .hasRole("ADMIN").anyRequest().hasRole("USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @PreDestroy
    public void shutdown() {
        // shutdown derby
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            logger.error("Derby is shutdown.", e);
        }
    }

}