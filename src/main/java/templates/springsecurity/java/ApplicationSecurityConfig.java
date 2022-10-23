package templates.springsecurity.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

public class ApplicationSecurityConfig  {

    private final PasswordEncoder passwordEncoder;
@Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((aut) -> aut
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        return http.build();
    }



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
    }


    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
    String encodedPassword= passwordEncoder.encode("password");
        UserDetails user = User.withUsername("jayrathod")
                .password(encodedPassword)
                .roles("USER") //this internally will be role_admin
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
