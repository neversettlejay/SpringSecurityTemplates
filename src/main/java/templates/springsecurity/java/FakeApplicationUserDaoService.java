package templates.springsecurity.java;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao {
    private final PasswordEncoder passwordEncoder;


    public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder){this.passwordEncoder=passwordEncoder;}

    @Override
    public Optional<AppliactionUser> selectApplicationUserByUsername(String username) {
// will return the first object found in the list of objects that has the matching username.
        return getListOfUsers().stream().filter(applicationUser ->
            username.equals(applicationUser.getUsername())).findFirst();
          
    }


    private List<AppliactionUser> getListOfUsers() { // we can fetch these list of users from the database with these attributes.
        List<AppliactionUser> applicationUsers = Lists.newArrayList(
                new AppliactionUser(
                        "student",
                        passwordEncoder.encode("student"),
                        ApplicationUserRole.STUDENT.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true),
                new AppliactionUser(
                        "admin",
                        passwordEncoder.encode("admin"),
                        ApplicationUserRole.ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true),
                new AppliactionUser(
                        "admintrainee",
                        passwordEncoder.encode("admintrainee"),
                        ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true));

        return applicationUsers;
    }
    
}
