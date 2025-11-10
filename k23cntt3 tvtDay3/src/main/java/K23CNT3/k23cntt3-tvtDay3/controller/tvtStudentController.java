package tvtDay03.controller;


import K23CNT3.tvtDay03.service.entity.tvtStudent;
import K23CNT3.tvtDay03.service.service.tvtServiceStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class tvtStudentController {
    @Autowired
    private tvtServiceStudent studentService;
    @GetMapping("/student-list")
    public List<tvtStudent> getAllStudents() {
        return studentService.getStudents();
    }
    @GetMapping("/student/{id}")
    public tvtStudent getAllStudents(@PathVariable String id)
    {
        Long param = Long.parseLong(id);
        return studentService.getStudent(param);
    }
    @PostMapping("/student-add")
    public tvtStudent addStudent(@RequestBody tvtStudent student)
    {
        return studentService.addStudent(student);
    }
    @PutMapping("/student/{id}")
    public tvtStudent updateStudent(@PathVariable String id,
                                    @RequestBody tvtStudent student) {
        Long param = Long.parseLong(id);
        return studentService.updateStudent(param,
                student);
    }
    @DeleteMapping("/student/{id}")
    public boolean deleteStudent(@PathVariable String id) {
        Long param = Long.parseLong(id);
        return studentService.deleteStudent(param);
    }
}
