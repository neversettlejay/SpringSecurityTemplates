package templates.springsecurity.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import templates.springsecurity.ApplicationUserPermission;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig  {

    private final PasswordEncoder passwordEncoder;
@Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // .authorizeHttpRequests((aut) -> aut.anyRequest().authenticated())
                .authorizeRequests()
                .antMatchers("/index", "/css","/js").permitAll()
                .antMatchers("/api/**").hasAuthority(ApplicationUserRole.STUDENT.name())
                // .antMatchers("/api/**").hasAnyRole(ApplicationUserRole.ADMINTRAINEE.name())
                .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                .antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(),ApplicationUserRole.ADMINTRAINEE.name())
                /*
                 * Order of Ant matchers matter like if we do ".antMatchers( "/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(),ApplicationUserRole.ADMINTRAINEE.name())" 
                 * in the very beginning of ant matchers as we have not specified which http method the admintrainee will get the right of put, post delete too instead of just read.
                 */
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic(withDefaults());

        return http.build();
    }



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        
        return (web) -> web.ignoring().antMatchers("/ignore1",   "/ignore2");
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails student = User.withUsername("student")
                .password(passwordEncoder.encode("student"))
                .roles(ApplicationUserRole.STUDENT.name()) //this internally will be role_student
                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
                .build();

                UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles(ApplicationUserRole.ADMIN.name()) //this internally will be role_admin
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
                .build();


                UserDetails admintrainee = User.withUsername("admintrainee")
                .password(passwordEncoder.encode("admintrainee"))
                .roles(ApplicationUserRole.ADMINTRAINEE.name()) //this internally will be role_admintrainee
                .authorities(ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities())
                .build();
        return new InMemoryUserDetailsManager(student, admin, admintrainee);
    }
}
