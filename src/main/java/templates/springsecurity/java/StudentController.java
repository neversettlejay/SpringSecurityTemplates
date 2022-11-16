package templates.springsecurity.java;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private static final List<Student> students= Arrays.asList(new Student(1,"Jay Rathod"), new Student(2, "What is a movie"));
    @GetMapping(path = "{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId){

        return students.stream().filter(student -> studentId.equals(student.getStudentId())).findFirst().orElseThrow(()->new IllegalStateException("Maybe no student is found with studentId" + studentId));

    }


}
