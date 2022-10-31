package templates.springsecurity;

import org.springframework.security.access.method.P;

public enum ApplicationUserPermission {
    
STUDENT_READ("student:read"),
STUDENT_WRITE("student:write"),
COURSE_READ("course:read"),
COURSE_WRITE("course:write");

    private final String permission;
    ApplicationUserPermission (String permission){
        this.permission=permission;
    }
}
