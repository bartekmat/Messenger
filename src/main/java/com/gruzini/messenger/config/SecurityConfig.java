package com.gruzini.messenger.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        what happens here:
        all endpoints are secured in the same manner - everything requires authentication
         */
        http.antMatcher("/**")
                .authorizeRequests(a -> a
                    .antMatchers("/**").permitAll()
                    .anyRequest().authenticated())
                .oauth2Login();
    }
}
