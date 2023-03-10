package com.quintor.api.mongoConnection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    public StudentService getStudentService() {
        return this.studentService;
    }

    @GetMapping("/all")
    public List<Student> fetchAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping("/insert")
    public void insertStudent() {
        studentService.insertStudent(new Student("test", "test", "test"));
    }


}
