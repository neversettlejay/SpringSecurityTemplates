package templates.springsecurity.java;

import java.util.Optional;

public interface ApplicationUserDao {
    Optional<AppliactionUser> selectApplicationUserByUsername(String username); 
}
