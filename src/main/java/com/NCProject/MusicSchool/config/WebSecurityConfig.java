package com.NCProject.MusicSchool.config;

import com.NCProject.MusicSchool.models.Role;
import com.NCProject.MusicSchool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    public UserService userService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/registration/**").not().fullyAuthenticated()
                    .antMatchers("/main/**").authenticated()
                    .antMatchers("/admin/**").hasRole(String.valueOf(Role.ADMIN))
                    .antMatchers("/student/**").hasRole(String.valueOf(Role.STUDENT))
                    .antMatchers("/teacher/**").hasRole(String.valueOf(Role.TEACHER))
                    .antMatchers("/attendance/**").hasRole(String.valueOf(Role.TEACHER))
                    .antMatchers("/chat/**").hasRole(String.valueOf(Role.USER))
                    .antMatchers("/uploads/**").hasRole(String.valueOf(Role.USER))
                    .antMatchers("/").permitAll()//на главную страничку мы разрешаем волный доступ
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();

    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
    }

}