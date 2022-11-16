package templates.springsecurity.java;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// purpose of this class is to verify its credentials 

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
        //below code is to authenticate the user NOT to generate a token, the successfulAuthentication method below will be invoked only when the attemptAuthentication  method has returned the authentication object successfully
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        // from httpservletrequest get the UsernameAndPasswordAuthenticationReqest
        // object and map it to javas object called
        // UsernameAndPasswordAuthenticationRequest

        try {
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword());
            Authentication authenticated=authenticationManager.authenticate(authentication);// this authentication manager will make sure the
                                                               // username exist and it will check whether the password
                                                               // is correct or not
            return authenticated;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws ServletException, IOException {
                //inside successfulAuthentication, JWT token will be created 
                
                
                // jwt contains header, payload and the signature so lets create below
                String jwtTokenCreation=Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(1)))
                .signWith(Keys.hmacShaKeyFor("thisisalongconfidentialkey".getBytes()))
                .compact();
                    // now we have generated the token now we need to send it to the client so lets create
                    response.addHeader("Authorization", jwtTokenCreation);  // like in video we havent sent 'Bearer token' we have just sent 'token'
                    

        // super.successfulAuthentication(request, response, chain, authResult);
    }
}