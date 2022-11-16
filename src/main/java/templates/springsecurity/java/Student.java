package templates.springsecurity.java;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@ToString
@Getter
@Setter
public class Student {

    private Integer studentId;
    private String studentName;

}
