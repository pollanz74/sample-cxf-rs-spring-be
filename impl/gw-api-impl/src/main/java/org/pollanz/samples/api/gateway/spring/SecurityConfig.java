package org.pollanz.samples.api.gateway.spring;

import lombok.extern.slf4j.Slf4j;
import org.pollanz.samples.api.gateway.security.UnauthorizedEntryPoint;
import org.pollanz.samples.api.gateway.security.jwt.JWTConfigurer;
import org.pollanz.samples.api.gateway.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("someuser").password("somepassword").roles("USER").build());
        manager.createUser(User.withUsername("anotheruser").password("anotherpassword").roles("ADMIN", "USER").build());
        manager.createUser(User.withUsername("guest").password("guest").roles("GUEST").build());
        return manager;
    }

    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider();
    }

    @Bean
    public UnauthorizedEntryPoint unauthorizedEntryPoint() {
        return new UnauthorizedEntryPoint();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/index.html")
                .antMatchers("/o2c.html")
                .antMatchers("/swagger-ui.js")
                .antMatchers("/swagger-ui.min.js")
                .antMatchers("/css/**")
                .antMatchers("/fonts/**")
                .antMatchers("/images/**")
                .antMatchers("/lang/**")
                .antMatchers("/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .rememberMe().disable()
                .headers().frameOptions().disable()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/rest/swagger.*").permitAll()
                .antMatchers(HttpMethod.POST, "/rest/user/authenticate").permitAll()
                .antMatchers("/rest/**").authenticated()
                .and()
                .apply(securityConfigurerAdapter());
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider());
    }

}
