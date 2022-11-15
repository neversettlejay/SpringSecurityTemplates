package templates.springsecurity.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import templates.springsecurity.ApplicationUserPermission;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig  {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
@Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService=applicationUserService;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        /*We disable Cross Site Request Forgery because we dont want any other user to send a malicious link that contains the operations that would do something in our system. Hence to be cautions we validate csrf token in every request.
        server sends csrf token after client request something and then in each operation the csrf token is sent with the request to validate the token to not let csrf happen.
        */ 
                .csrf().disable()
                // .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // .and() //this enables csrf
                // .authorizeHttpRequests((aut) -> aut.anyRequest().authenticated())
                .authorizeRequests()
                .antMatchers("/index", "/css","/js").permitAll()
                .antMatchers("/api/**").hasAuthority(ApplicationUserRole.STUDENT.name())
                // .antMatchers("/api/**").hasAnyRole(ApplicationUserRole.ADMINTRAINEE.name())
                // .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                // .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                // .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
                // .antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(),ApplicationUserRole.ADMINTRAINEE.name())
                /*
                 * Order of Ant matchers matter like if we do ".antMatchers( "/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(),ApplicationUserRole.ADMINTRAINEE.name())" 
                 * in the very beginning of ant matchers as we have not specified which http method the admintrainee will get the right of put, post delete too instead of just read.
                 */
                .anyRequest()
                .authenticated()
                .and()
                // .httpBasic(withDefaults());// for basic authentication
                .formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses",true)
                .passwordParameter("password-jay")
                .usernameParameter("username-jay")
                .and()
                .rememberMe().tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)).key("somethingVerySecure")//key is used to generate the expiration date to store in the remember me cookie that contains username, expiration time and md5 hash of them.
                /*remember me is used because sessionid which is responsible for authentication expires in 30 minutes of inactivity so remember me defaults to 2 weeks. */
                .rememberMeParameter("remember-me-jay")
                .and()
                .logout()
                .logoutUrl("/logout")//if csrf is enabled then we need to do logoutUrl post method request to logout, if csrf is disabled then its okay to use any http method
                // .logoutRequestMatcher(new AntPathRequestMatcher("logout","GET"))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("remember-me", "JSESSIONID")
                .logoutSuccessUrl("/login")
                ;//for formbased authentication

                http.authenticationProvider(daoAuthenticationProvider());

        return http.build();
    }



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        
        return (web) -> web.ignoring().antMatchers("/ignore1",   "/ignore2");
    }
// here we can override JdbcDaoImpl too to store users in the database.
    // @Bean
    // public InMemoryUserDetailsManager userDetailsService() {
    //     UserDetails student = User.withUsername("student")
    //             .password(passwordEncoder.encode("student"))
    //             .roles(ApplicationUserRole.STUDENT.name()) //this internally will be role_student
    //             .authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
    //             .build();

    //             UserDetails admin = User.withUsername("admin")
    //             .password(passwordEncoder.encode("admin"))
    //             .roles(ApplicationUserRole.ADMIN.name()) //this internally will be role_admin
    //             .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
    //             .build();
                


    //             UserDetails admintrainee = User.withUsername("admintrainee")
    //             .password(passwordEncoder.encode("admintrainee"))
    //             .roles(ApplicationUserRole.ADMINTRAINEE.name()) //this internally will be role_admintrainee
    //             .authorities(ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities())
    //             .build();
    //     return new InMemoryUserDetailsManager(student, admin, admintrainee);
    // }

    
        @Bean
        public DaoAuthenticationProvider daoAuthenticationProvider(){
            DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
            daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
            daoAuthenticationProvider.setUserDetailsService(applicationUserService);


            return daoAuthenticationProvider;


        }

}
