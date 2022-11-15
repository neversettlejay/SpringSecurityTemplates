package templates.springsecurity.java;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.Lists;

public class FakeApplicationUserDaoService implements ApplicationUserDao {
    private final PasswordEncoder passwordEncoder;


    public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder){this.passwordEncoder=passwordEncoder;}

    @Override
    public Optional<AppliactionUser> selectApplicationUserByUsername(String username) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }


    private List<AppliactionUser> getListOfUsers(){
        List<AppliactionUser> list=Lists.newArrayList(

        new AppliactionUser(
                        ApplicationUserRole.STUDENT.getGrantedAuthorities()
                        , passwordEncoder.encode("password")
                        , "student"
                        , false
                        , false
                        , false
                        , false)                        


        );

    }
    
}
