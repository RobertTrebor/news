package de.gainc.news;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/news**/*").authenticated()
        .antMatchers("/news").authenticated()
        .anyRequest().permitAll()
        .and()
        .csrf().ignoringAntMatchers("/h2-console/*", "/api/**")
        .and()
        .headers().frameOptions().disable()
        .and()
        .formLogin();
  }

}
