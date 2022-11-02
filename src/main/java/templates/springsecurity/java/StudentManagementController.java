package templates.springsecurity.java;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/management/api/v1/students")
@Slf4j
public class StudentManagementController {
    private static List<Student> students= Arrays.asList(new Student(1,"Jay Rathod"), new Student(2, "Jay Billionaire"), new Student(3, "Jay Millionaire"));
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_ADMINTRAINEE')")
    public List<Student> getAllStudents(){
        return students;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void registerNewStudent(Student student){
      log.info("Student added " + student +"to the list"); 

    }

    @DeleteMapping(path="{id}")
    @PreAuthorize("hasAuthority('student:write')")
    public void deleteStudent(@PathVariable("id") Integer id){
        // Student studDelete = new Student();
        log.info("Student deleted to verify autherization");
        

    }
    @PutMapping(path="{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent( @PathVariable("studentId") Integer id, @RequestBody Student student){
        log.info("Student modified");
    }


}
